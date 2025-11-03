plugins {
    id("java")
}

group = "net.cubizor.cubicolor"
// Version from gradle.properties (managed by semantic-release)
version = project.findProperty("version") ?: "0.0.0-dev"

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
                // Nexus repository (sadece CI'da kullanılır)
                val nexusReleaseUrl = System.getenv("NEXUS_RELEASE_URL")
                val nexusSnapshotUrl = System.getenv("NEXUS_SNAPSHOT_URL")

                if (nexusReleaseUrl != null && nexusSnapshotUrl != null) {
                    maven {
                        url = uri(if (version.toString().endsWith("SNAPSHOT")) nexusSnapshotUrl else nexusReleaseUrl)

                        credentials {
                            username = System.getenv("NEXUS_USERNAME")
                            password = System.getenv("NEXUS_PASSWORD")
                        }
                    }
                }
            }
        }
    }
}
