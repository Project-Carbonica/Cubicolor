plugins {
    id("java")
    id("pl.allegro.tech.build.axion-release") version "1.21.0"
}

// Axion-release yapılandırması (GitHub Actions ile otomatik versiyonlama)
scmVersion {
    tag {
        prefix.set("v")
        versionSeparator.set("")
    }

    versionCreator { versionFromTag, position ->
        val baseVersion = versionFromTag.split("-").first()
        if (position.branch == "main" || position.branch == "master") {
            baseVersion
        } else {
            "$baseVersion-${position.branch}-SNAPSHOT"
        }
    }

    // GitHub Actions için ayarlar
    checks {
        // CI ortamında uncommitted changes kontrolünü devre dışı bırak
        uncommittedChanges.set(false)
    }
}

group = "net.cubizor.cubicolor"
version = scmVersion.version

// Alt projeler için ortak yapılandırma
subprojects {
    version = rootProject.version
    group = rootProject.group

    // Maven publish yapılandırması
    apply(plugin = "maven-publish")

    afterEvaluate {
        extensions.configure<PublishingExtension>("publishing") {
            publications {
                create<MavenPublication>("maven") {
                    from(components["java"])

                    // POM metadata
                    pom {
                        name.set("${rootProject.name} - ${project.name}")
                        description.set("Cubicolor - Modern color management library for Minecraft")
                        url.set("https://github.com/Cubizor/Cubicolor")

                        licenses {
                            license {
                                name.set("MIT License")
                                url.set("https://opensource.org/licenses/MIT")
                            }
                        }

                        developers {
                            developer {
                                id.set("cubizor")
                                name.set("Cubizor")
                            }
                        }

                        scm {
                            connection.set("scm:git:git://github.com/Cubizor/Cubicolor.git")
                            developerConnection.set("scm:git:ssh://github.com/Cubizor/Cubicolor.git")
                            url.set("https://github.com/Cubizor/Cubicolor")
                        }
                    }
                }
            }

            repositories {
                maven {
                    val releasesRepoUrl = System.getenv("NEXUS_RELEASE_URL") ?: ""
                    val snapshotsRepoUrl = System.getenv("NEXUS_SNAPSHOT_URL") ?: ""
                    url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                    credentials {
                        username = System.getenv("NEXUS_USERNAME")
                        password = System.getenv("NEXUS_PASSWORD")
                    }
                }
            }
        }
    }
}
