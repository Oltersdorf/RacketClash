plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(project(":database"))
    implementation(project(":language"))
    implementation(project(":util:ui"))
    implementation(project(":util:state"))
    implementation(project(":app:state"))
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(libs.compose.navigation)
}