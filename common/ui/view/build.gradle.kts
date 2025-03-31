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
    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(libs.compose.navigation)
    implementation(project(":common:ui:base"))
    implementation(project(":common:databaseApi"))
    implementation(project(":common:state"))
    implementation(project(":common:model:addSchedule"))
    implementation(project(":common:model:schedule"))
}