plugins {
    id("java")
}

group = "net.cubizor.cubicolor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Project dependencies
    implementation(project(":cubicolor-api"))
    implementation(project(":cubicolor-core"))
    implementation(project(":cubicolor-text"))

    // JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}