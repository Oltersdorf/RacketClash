package dev.oltersdorf.racketclash.server.routing

import dev.oltersdorf.racketclash.database.api.item.Player
import dev.oltersdorf.racketclash.database.api.item.PlayerFilter
import dev.oltersdorf.racketclash.database.api.item.PlayerSorting
import dev.oltersdorf.racketclash.database.api.item.PlayerTable
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class PlayersTest : TableBaseTest<Player, PlayerFilter, PlayerSorting, PlayersTest.PlayerTableStore>(
    installRoutes = { playersRoutes(it) },
    baseUrl = "/players"
) {
    class PlayerTableStore(initialStore: List<Player> = emptyList()) :
        PlayerTable, TableBaseImpl<Player, PlayerFilter, PlayerSorting>(initialStore) {

        override suspend fun clubs(filter: String): List<String> =
            listOf("Club1")
    }

    @Test
    override fun `insert test`() =
        insertTestImpl(
            tableStore = PlayerTableStore(),
            itemToInsert = Player(id = 10L, "insertMe")
        )

    @Test
    override fun `successful delete test`() =
        successfulDeleteTestImpl(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "deleteMe"))),
        )

    @Test
    override fun `unsuccessful delete test`() =
        unsuccessfulDeleteTestImpl(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "deleteMe"))),
            deleteId = 1L
        )

    @Test
    override fun `invalid id delete test`() =
        invalidIdDeleteTestImpl(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "deleteMe")))
        )

    @Test
    override fun `successful update test`() =
        successfulUpdateTestImpl(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "updateMe"))),
            updatedItem = Player(id = 10L, "updated")
        )

    @Test
    override fun `unsuccessful update test`() =
        unsuccessfulUpdateTestImpl(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "updateMe"))),
            updatedItem = Player(id = 1L, "updated")
        )

    @Test
    override fun `successful selectSingle test`() =
        successfulSelectSingleTestImpl<Player>(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "selectMe"))),
        )

    @Test
    override fun `unsuccessful selectSingle test`() =
        unsuccessfulSelectSingleTestImpl(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "selectMe"))),
            requestedId = 1L
        )

    @Test
    override fun `invalid id selectSingle test`() =
        invalidIdSelectSingleTestImpl(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "selectMe")))
        )

    @Test
    override fun `successful selectLast test`() =
        successfulSelectLastTestImpl<Player>(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "selectMe")))
        )

    @Test
    override fun `invalid number selectLast test`() =
        invalidNumberSelectLastTestImpl(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "selectMe")))
        )

    @Test
    override fun `selectSortedAndFiltered test`() =
        selectSortedAndFilteredTestImpl<Player, PlayerFilter, PlayerSorting>(
            tableStore = PlayerTableStore(initialStore = listOf(Player(id = 10L, "selectMe"))),
            sortedAndFilteredQuery = SortedAndFilteredQuery(
                filter = PlayerFilter(),
                sorting = PlayerSorting.NameAsc,
                fromIndex = 100,
                limit = 200
            )
        )

    @Test
    fun `successful clubs test`() =
        storeTestApplication(tableStore = PlayerTableStore()) {
            val response = client.get("$baseUrl/clubs/test")

            assertEquals(HttpStatusCode.OK, response.status)
            assertContentEquals(listOf("Club1"), response.body<List<String>>())
        }
}