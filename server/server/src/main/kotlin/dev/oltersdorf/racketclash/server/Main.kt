package dev.oltersdorf.racketclash.server

import dev.oltersdorf.racketclash.server.routing.configureRouting
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

internal fun Application.module() {
    configureRouting()
    configureContentNegotiation()
}