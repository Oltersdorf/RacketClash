import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.sqldelight)
}

group = "com.oltersdorf.racketclash"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(libs.coroutines)
    implementation(libs.bundles.sqlite)
    implementation(libs.compose.navigation)

    testImplementation(compose.desktop.uiTestJUnit4)
    testImplementation(compose.desktop.currentOs)
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

sqldelight {
    databases {
        create("RacketClashDatabase") {
            packageName.set("com.olt.racketclash.database")
        }
    }
}
