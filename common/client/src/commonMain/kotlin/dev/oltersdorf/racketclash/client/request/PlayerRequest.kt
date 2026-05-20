package dev.oltersdorf.racketclash.client.request

import dev.oltersdorf.racketclash.database.api.TableBase
import dev.oltersdorf.racketclash.database.api.item.Player
import dev.oltersdorf.racketclash.database.api.item.PlayerFilter
import dev.oltersdorf.racketclash.database.api.item.PlayerSorting
import dev.oltersdorf.racketclash.database.api.item.PlayerTable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

internal class PlayerRequestImpl(
    client: HttpClient,
    host: String
) : TableBaseRequest<Player, PlayerFilter, PlayerSorting>(
    client = client,
    url = "$host/players"
) {
    override fun HttpRequestBuilder.itemToBody(item: Player) =
        setBody(item)

    override suspend fun HttpResponse.bodyToItem(): Player =
        body<Player>()
}

class PlayerRequest internal constructor(
    private val client: HttpClient,
    private val host: String
) : PlayerTable, TableBase<Player, PlayerFilter, PlayerSorting> by PlayerRequestImpl(
    client = client,
    host = host
) {
    override suspend fun clubs(filter: String): List<String> {
        val response = client.get(urlString = "$host/clubs/$filter")

        return if (response.status == HttpStatusCode.OK)
            response.body<List<String>>()
        else
            emptyList()
    }
}