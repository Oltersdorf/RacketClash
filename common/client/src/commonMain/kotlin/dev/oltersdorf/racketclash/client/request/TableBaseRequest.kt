package dev.oltersdorf.racketclash.client.request

import dev.oltersdorf.racketclash.database.api.FilteredSortedList
import dev.oltersdorf.racketclash.database.api.IdItem
import dev.oltersdorf.racketclash.database.api.TableBase
import dev.oltersdorf.racketclash.server.api.SortedAndFilteredQuery
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

internal abstract class TableBaseRequest<Item: IdItem, Filter, Sorting>(
    protected val client: HttpClient,
    protected val url: String
) : TableBase<Item, Filter, Sorting> {

    protected abstract fun HttpRequestBuilder.itemToBody(item: Item)

    protected abstract suspend fun HttpResponse.bodyToItem(): Item

    override suspend fun insert(data: Item): Long {
        val response = client.post(urlString = url) {
            contentType(ContentType.Application.Json)
            itemToBody(data)
        }

        return if (response.status == HttpStatusCode.Created)
            response.body<Long>()
        else
            -1L
    }

    override suspend fun delete(id: Long): Boolean {
        val response = client.delete(urlString = "$url/$id")

        return response.status == HttpStatusCode.OK
    }

    override suspend fun update(data: Item): Boolean {
        val response = client.put(urlString = url) {
            contentType(ContentType.Application.Json)
            itemToBody(data)
        }

        return response.status == HttpStatusCode.OK
    }

    override suspend fun selectSingle(id: Long): Item? {
        val response = client.get(urlString = "$url/$id")

        return if (response.status == HttpStatusCode.OK)
            response.bodyToItem()
        else
            null
    }

    override suspend fun selectLast(n: Int): List<Item> {
        val response = client.get(urlString = "$url/last/$n")

        return if (response.status == HttpStatusCode.OK)
            response.body<List<Item>>()
        else
            emptyList()
    }

    override suspend fun selectSortedAndFiltered(
        filter: Filter,
        sorting: Sorting,
        fromIndex: Long,
        limit: Int
    ): FilteredSortedList<Item, Filter, Sorting> {
        val response = client.post(urlString = "$url/list") {
            contentType(ContentType.Application.Json)
            setBody(
                SortedAndFilteredQuery(
                    filter = filter,
                    sorting = sorting,
                    fromIndex = fromIndex,
                    limit = limit
                )
            )
        }

        return if (response.status == HttpStatusCode.OK)
            response.body<FilteredSortedList<Item, Filter, Sorting>>()
        else
            FilteredSortedList(
                totalItemsInDatabase = -1,
                fromIndex = fromIndex,
                limit = limit,
                items = emptyList(),
                filter = filter,
                sorting = sorting
            )
    }
}