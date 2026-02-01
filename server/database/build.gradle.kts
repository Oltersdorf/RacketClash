plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":common:databaseApi"))
    implementation(libs.logback)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgresql)
    testImplementation(kotlin("test"))
    testImplementation(libs.testcontainer.postgresql)
    testImplementation(libs.testcontainer.junit)
}

tasks {
    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(17)
}
