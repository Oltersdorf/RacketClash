plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":common:database"))
    implementation(project(":common:util:state"))
    implementation(libs.compose.navigation)
}