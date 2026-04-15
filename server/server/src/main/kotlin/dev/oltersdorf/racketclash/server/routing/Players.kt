package dev.oltersdorf.racketclash.server.routing

import dev.oltersdorf.racketclash.database.api.item.PlayerTable
import dev.oltersdorf.racketclash.database.table.PlayerTableInterface
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

internal fun Route.playersRoutes(playerTable: PlayerTable = PlayerTableInterface) {
    tableBaseRoutes("/players", playerTable)
    get("/players/clubs/{nameFilter}") {
        val filter = call.parameters["nameFilter"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        call.respond(playerTable.clubs(filter = filter))
    }
}