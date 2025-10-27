plugins {
    id("java")
}

group = "net.cubizor.cubicolor"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":cubicolor-api"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}