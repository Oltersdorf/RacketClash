package dev.oltersdorf.racketclash.server.routing

import dev.oltersdorf.racketclash.database.databasePostgresConnect
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

internal fun Application.configureRouting() {
    databasePostgresConnect(
        url = "jdbc:postgresql://localhost:5432/racketClash",
        user = "root",
        password = "1234"
    )

    routing {
        clubsRoutes()
        playersRoutes()
    }
}