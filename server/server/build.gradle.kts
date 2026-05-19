import io.ktor.plugin.features.DockerImageRegistry

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":common:databaseApi"))
    implementation(project(":common:serverApi"))
    implementation(project(":server:database"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logback)

    testImplementation(kotlin("test"))
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)
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