import io.ktor.plugin.features.DockerImageRegistry

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

dependencies {
    implementation(project(":common:databaseApi"))
    implementation(libs.ktor.netty)
    implementation(libs.logback)
}

kotlin {
    jvmToolchain(17)
}

ktor {
    docker {
        jreVersion = JavaVersion.VERSION_17
        localImageName = "racket-clash-docker"
        imageTag = "test"

        externalRegistry = DockerImageRegistry.dockerHub(
            appName = provider { "ktor-app" },
            username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
            password = providers.environmentVariable("DOCKER_HUB_PASSWORD"),
        )
    }
}