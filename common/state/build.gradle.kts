plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":common:databaseApi"))
    implementation(libs.compose.navigation)
}