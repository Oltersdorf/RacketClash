import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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
    implementation(project(":common:database"))
    implementation(project(":common:databaseApi"))
    implementation(project(":common:ui:view"))
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
}

compose.desktop {
    application {
        mainClass = "com.olt.racketclash.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "RacketClash"
            packageVersion = "1.0.0"
            modules("java.instrument", "java.sql", "jdk.unsupported")
        }
    }
}