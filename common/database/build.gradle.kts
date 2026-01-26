plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:databaseApi"))
                implementation(libs.exposed.core)
                implementation(libs.exposed.jdbc)
            }
        }
    }
}