plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":common:database"))
    implementation(libs.compose.navigation)
}