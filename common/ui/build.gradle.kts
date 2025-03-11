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
    implementation(project(":common:util:state"))
    implementation(project(":common:database"))
    implementation(project(":common:model:addOrUpdateCategory"))
    implementation(project(":common:model:addOrUpdatePlayer"))
    implementation(project(":common:model:addOrUpdateRule"))
    implementation(project(":common:model:addOrUpdateTeam"))
    implementation(project(":common:model:addOrUpdateTournament"))
    implementation(project(":common:model:addSchedule"))
    implementation(project(":common:model:categories"))
    implementation(project(":common:model:category"))
    implementation(project(":common:model:player"))
    implementation(project(":common:model:players"))
    implementation(project(":common:model:rules"))
    implementation(project(":common:model:schedule"))
    implementation(project(":common:model:team"))
    implementation(project(":common:model:teams"))
    implementation(project(":common:model:tournaments"))
    implementation(project(":common:model:start"))
}