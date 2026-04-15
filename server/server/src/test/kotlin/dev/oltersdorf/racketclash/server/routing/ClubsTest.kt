package dev.oltersdorf.racketclash.server.routing

import dev.oltersdorf.racketclash.database.api.item.Club
import dev.oltersdorf.racketclash.database.api.item.ClubFilter
import dev.oltersdorf.racketclash.database.api.item.ClubSorting
import dev.oltersdorf.racketclash.database.api.item.ClubTable
import kotlin.test.Test

internal class ClubsTest : TableBaseTest<Club, ClubFilter, ClubSorting, ClubsTest.ClubTableStore>(
    installRoutes = { clubsRoutes(it) },
    baseUrl = "/clubs"
) {
    class ClubTableStore(initialStore: List<Club> = emptyList()) :
        ClubTable, TableBaseImpl<Club, ClubFilter, ClubSorting>(initialStore)

    @Test
    override fun `insert test`() =
        insertTestImpl(
            tableStore = ClubTableStore(),
            itemToInsert = Club(id = 10L, name = "insertMe")
        )

    @Test
    override fun `successful delete test`() =
        successfulDeleteTestImpl(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "deleteMe")))
        )

    @Test
    override fun `unsuccessful delete test`() =
        unsuccessfulDeleteTestImpl(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "deleteMe"))),
            deleteId = 1L
        )

    @Test
    override fun `invalid id delete test`() =
        invalidIdDeleteTestImpl(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "deleteMe")))
        )

    @Test
    override fun `successful update test`() =
        successfulUpdateTestImpl(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "updateMe"))),
            updatedItem = Club(id = 10L, name = "updated")
        )

    @Test
    override fun `unsuccessful update test`() =
        unsuccessfulUpdateTestImpl(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "updateMe"))),
            updatedItem = Club(id = 1L, name = "updated")
        )

    @Test
    override fun `successful selectSingle test`() =
        successfulSelectSingleTestImpl<Club>(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "selectMe")))
        )

    @Test
    override fun `unsuccessful selectSingle test`() =
        unsuccessfulSelectSingleTestImpl(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "selectMe"))),
            requestedId = 1L
        )

    @Test
    override fun `invalid id selectSingle test`() =
        invalidIdSelectSingleTestImpl(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "selectMe")))
        )

    @Test
    override fun `successful selectLast test`() =
        successfulSelectLastTestImpl<Club>(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "selectMe")))
        )

    @Test
    override fun `invalid number selectLast test`() =
        invalidNumberSelectLastTestImpl(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "selectMe")))
        )

    @Test
    override fun `selectSortedAndFiltered test`() =
        selectSortedAndFilteredTestImpl<Club, ClubFilter, ClubSorting>(
            tableStore = ClubTableStore(initialStore = listOf(Club(id = 10L, name = "selectMe"))),
            sortedAndFilteredQuery = SortedAndFilteredQuery(
                filter = ClubFilter(),
                sorting = ClubSorting.NameAsc,
                fromIndex = 100,
                limit = 200
            )
        )
}