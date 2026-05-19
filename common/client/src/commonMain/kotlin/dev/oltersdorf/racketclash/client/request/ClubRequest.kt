package dev.oltersdorf.racketclash.client.request

import dev.oltersdorf.racketclash.database.api.TableBase
import dev.oltersdorf.racketclash.database.api.item.Club
import dev.oltersdorf.racketclash.database.api.item.ClubFilter
import dev.oltersdorf.racketclash.database.api.item.ClubSorting
import dev.oltersdorf.racketclash.database.api.item.ClubTable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

internal class ClubRequestImpl(
    client: HttpClient,
    host: String
) : TableBaseRequest<Club, ClubFilter, ClubSorting>(
    client = client,
    url = "$host/clubs"
) {
    override fun HttpRequestBuilder.itemToBody(item: Club) =
        setBody(item)

    override suspend fun HttpResponse.bodyToItem(): Club =
        body<Club>()
}

class ClubRequest internal constructor(
    private val client: HttpClient,
    private val host: String
) : ClubTable, TableBase<Club, ClubFilter, ClubSorting> by ClubRequestImpl(
    client = client,
    host = host
)
