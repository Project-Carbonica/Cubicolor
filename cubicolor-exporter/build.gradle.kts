plugins {
    id("java")
}

group = "net.cubizor.cubicolor"

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
}
