plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

group = "net.cubizor.cubicolor"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":cubicolor-api"))
    api(project(":cubicolor-core"))
    api(project(":cubicolor-text"))
    api(project(":cubicolor-manager"))
    api(project(":cubicolor-exporter"))

    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")

}


tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}