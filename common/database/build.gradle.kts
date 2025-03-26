plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.sqldelight)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.coroutines)
    implementation(libs.bundles.sqlite)
    testImplementation(kotlin("test"))
    implementation(project(":common:databaseApi"))
}

sqldelight {
    databases {
        create("RacketClashDatabase") {
            packageName.set("com.olt.racketclash.database")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}