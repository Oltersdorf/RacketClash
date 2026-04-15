package dev.oltersdorf.racketclash.server.routing

import dev.oltersdorf.racketclash.database.api.FilteredSortedList
import dev.oltersdorf.racketclash.database.api.IdItem
import dev.oltersdorf.racketclash.server.configureContentNegotiation
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.Route
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal abstract class TableBaseTest<Item: IdItem, Filter, Sorting, Table: TableBaseImpl<Item, Filter, Sorting>>(
    private val installRoutes: Route.(Table) -> Unit,
    val baseUrl: String
) {
    fun storeTestApplication(
        tableStore: Table,
        block: suspend ApplicationTestBuilder.(List<Item>) -> Unit
    ) {
        testApplication {
            application {
                routing {
                    installRoutes(tableStore)
                }
                configureContentNegotiation()
            }
            client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            block(tableStore.store)
        }
    }

    abstract fun `insert test`()

    inline fun <reified Item> insertTestImpl(
        tableStore: Table,
        itemToInsert: Item
    ) =
        storeTestApplication(tableStore) {
            val response = client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(itemToInsert)
            }

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(0L, response.body<Long>())
        }

    abstract fun `successful delete test`()

    fun successfulDeleteTestImpl(tableStore: Table) =
        storeTestApplication(tableStore) {
            val response = client.delete("$baseUrl/${it.first().id}")

            assertEquals(HttpStatusCode.OK, response.status)
        }

    abstract fun `unsuccessful delete test`()

    fun unsuccessfulDeleteTestImpl(
        tableStore: Table,
        deleteId: Long
    ) =
        storeTestApplication(tableStore) {
        val response = client.delete("$baseUrl/$deleteId")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    abstract fun `invalid id delete test`()

    fun invalidIdDeleteTestImpl(tableStore: Table) =
        storeTestApplication(tableStore) {
            val response = client.delete("$baseUrl/one")

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

    abstract fun `successful update test`()

    inline fun <reified Item> successfulUpdateTestImpl(
        tableStore: Table,
        updatedItem: Item
    ) =
        storeTestApplication(tableStore) {
            val response = client.put(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(updatedItem)
            }

            assertEquals(HttpStatusCode.OK, response.status)
        }

    abstract fun `unsuccessful update test`()

    inline fun <reified Item> unsuccessfulUpdateTestImpl(
        tableStore: Table,
        updatedItem: Item
    ) =
        storeTestApplication(tableStore) {
            val response = client.put(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(updatedItem)
            }

            assertEquals(HttpStatusCode.NotFound, response.status)
        }

    abstract fun `successful selectSingle test`()

    inline fun <reified Item> successfulSelectSingleTestImpl(tableStore: Table) =
        storeTestApplication(tableStore) {
            val response = client.get("$baseUrl/${it.first().id}")

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(it.first() as Item, response.body<Item>())
        }

    abstract fun `unsuccessful selectSingle test`()

    fun unsuccessfulSelectSingleTestImpl(
        tableStore: Table,
        requestedId: Long
    ) =
        storeTestApplication(tableStore) {
            val response = client.get("$baseUrl/$requestedId")

            assertEquals(HttpStatusCode.NotFound, response.status)
        }

    abstract fun `invalid id selectSingle test`()

    fun invalidIdSelectSingleTestImpl(tableStore: Table) =
        storeTestApplication(tableStore) {
            val response = client.get("$baseUrl/one")

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

    abstract fun `successful selectLast test`()

    inline fun <reified Item> successfulSelectLastTestImpl(tableStore: Table) =
        storeTestApplication(tableStore) {
            val response = client.get("$baseUrl/last/1")

            assertEquals(HttpStatusCode.OK, response.status)
            assertContentEquals(it as List<*>, response.body<List<Item>>())
        }

    abstract fun `invalid number selectLast test`()

    fun invalidNumberSelectLastTestImpl(tableStore: Table) =
        storeTestApplication(tableStore) {
            val response = client.get("$baseUrl/last/one")

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

    abstract fun `selectSortedAndFiltered test`()

    inline fun <reified Item, reified Filter, reified Sorting> selectSortedAndFilteredTestImpl(
        tableStore: Table,
        sortedAndFilteredQuery: SortedAndFilteredQuery<Filter, Sorting>,
    ) =
        storeTestApplication(tableStore) {
            val response = client.post("$baseUrl/list") {
                contentType(ContentType.Application.Json)
                setBody(sortedAndFilteredQuery)
            }

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(
                FilteredSortedList(
                    totalItemsInDatabase = it.size.toLong(),
                    fromIndex = sortedAndFilteredQuery.fromIndex,
                    limit = sortedAndFilteredQuery.limit,
                    items = it,
                    filter = sortedAndFilteredQuery.filter,
                    sorting = sortedAndFilteredQuery.sorting
                ),
                response.body<FilteredSortedList<Item, Filter, Sorting>>() as FilteredSortedList<*, *, *>
            )
        }
}