plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.coroutines)
    implementation(libs.compose.navigation)
}