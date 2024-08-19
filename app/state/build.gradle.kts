plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(project(":database"))
    implementation(project(":language"))
    implementation(project(":util:state"))
    implementation(libs.compose.navigation)
}