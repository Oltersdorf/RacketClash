package dev.oltersdorf.racketclash.server.routing

import dev.oltersdorf.racketclash.database.api.item.ClubTable
import dev.oltersdorf.racketclash.database.table.ClubTableInterface
import io.ktor.server.routing.Route

internal fun Route.clubsRoutes(clubTable: ClubTable = ClubTableInterface) {
    tableBaseRoutes("/clubs", clubTable)
}