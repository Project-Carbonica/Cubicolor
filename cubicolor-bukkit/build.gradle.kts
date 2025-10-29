plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

group = "net.cubizor.cubicolor"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":cubicolor-api"))
    implementation(project(":cubicolor-core"))
    implementation(project(":cubicolor-text"))
    implementation(project(":cubicolor-manager"))
    implementation(project(":cubicolor-exporter"))

    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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