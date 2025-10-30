@file:Suppress("unused", "UnusedImport")
import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

version = "2025.07"

project {
    buildType(CubicolorBuild)
}

object CubicolorBuild : BuildType({
    name = "Cubicolor Build & Release"
    description = "Complete CI/CD pipeline for Cubicolor"

    vcs {
        root(DslContext.settingsRoot)

        // Clean checkout for reproducible builds
        cleanCheckout = true

        // Show changes from dependencies
        showDependenciesChanges = true
    }

    triggers {
        vcs {
            branchFilter = "+:main"
            enableQueueOptimization = false
        }
    }

    params {
        // Nexus repository configuration
        param("env.NEXUS_RELEASE_URL", "%nexus.release.url%")
        param("env.NEXUS_SNAPSHOT_URL", "%nexus.snapshot.url%")
        param("env.NEXUS_USERNAME", "%nexus.username%")
        param("env.NEXUS_PASSWORD", "%nexus.password%")

        // GitHub token for semantic-release
        param("env.GITHUB_TOKEN", "%github.token%")
        param("env.GH_TOKEN", "%github.token%")

        // Git author info (for semantic-release commits)
        param("env.GIT_AUTHOR_NAME", "TeamCity CI")
        param("env.GIT_AUTHOR_EMAIL", "ci@cubizor.net")
        param("env.GIT_COMMITTER_NAME", "TeamCity CI")
        param("env.GIT_COMMITTER_EMAIL", "ci@cubizor.net")
    }

    steps {
        // Step 1: Build & Test
        gradle {
            name = "Build & Test"
            id = "build_and_test"
            tasks = "clean build test"
            gradleParams = "--no-daemon --stacktrace"
            jdkHome = "%env.JDK_21_0%"
        }

        // Step 2: Install Node.js dependencies for semantic-release
        nodeJS {
            name = "Install Dependencies"
            id = "install_deps"
            shellScript = """
                cd .teamcity
                npm ci
            """.trimIndent()
        }

        // Step 3: Semantic Release (requires script for VERSION handling)
        script {
            name = "Semantic Release"
            id = "semantic_release"
            scriptContent = """
                #!/bin/bash
                set -euo pipefail

                echo "Configuring git for semantic-release..."
                git config user.name "${'$'}GIT_AUTHOR_NAME"
                git config user.email "${'$'}GIT_AUTHOR_EMAIL"

                echo "Running semantic-release from .teamcity directory..."
                cd .teamcity
                npx semantic-release

                # Read VERSION file if created by semantic-release (in project root)
                if [ -f ../VERSION ]; then
                    PROJECT_VERSION=${'$'}(cat ../VERSION)
                    echo "##teamcity[setParameter name='env.PROJECT_VERSION' value='${'$'}PROJECT_VERSION']"
                    echo "New version released: ${'$'}PROJECT_VERSION"
                else
                    echo "##teamcity[buildProblem description='No new version to release' typeId='NO_RELEASE']"
                    echo "##teamcity[buildStop comment='No changes requiring release' readdToQueue='false']"
                    exit 0
                fi
            """.trimIndent()
        }

        // Step 4: Build Versioned JAR
        gradle {
            name = "Build Versioned JAR"
            id = "build_versioned_jar"
            tasks = "clean build"
            gradleParams = "--no-daemon --stacktrace"
            jdkHome = "%env.JDK_21_0%"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }

        // Step 5: Publish to Nexus
        gradle {
            name = "Publish to Nexus"
            id = "publish_nexus"
            tasks = "publish"
            gradleParams = "--no-daemon --stacktrace"
            jdkHome = "%env.JDK_21_0%"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }

        // Step 6: Cleanup (pure Kotlin - using gradle)
        gradle {
            name = "Cleanup"
            id = "cleanup"
            tasks = "clean"
            gradleParams = "--no-daemon"
            jdkHome = "%env.JDK_21_0%"
            executionMode = BuildStep.ExecutionMode.ALWAYS
        }
    }

    features {
        commitStatusPublisher {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "%github.token%"
                }
            }
        }

        // Automatically set build status based on test results
        feature {
            type = "xml-report-plugin"
            param("xmlReportParsing.reportType", "junit")
            param("xmlReportParsing.reportDirs", "+:**/build/test-results/**/*.xml")
        }
    }

    failureConditions {
        executionTimeoutMin = 30

        // Fail if no tests were run
        failOnMetricChange {
            metric = BuildFailureOnMetric.MetricType.TEST_COUNT
            threshold = 0
            units = BuildFailureOnMetric.MetricUnit.DEFAULT_UNIT
            comparison = BuildFailureOnMetric.MetricComparison.LESS
            compareTo = value()
        }
    }

    requirements {
        // Require Linux agents
        contains("teamcity.agent.jvm.os.name", "Linux")

        // Require Node.js
        exists("teamcity.tool.nodejs")

        // Require JDK 17+
        exists("env.JDK_17_0")
    }

    // Artifact publishing
    artifactRules = """
        cubicolor-*/build/libs/*.jar => artifacts/
        CHANGELOG.md => artifacts/
        VERSION => artifacts/
    """.trimIndent()
})
