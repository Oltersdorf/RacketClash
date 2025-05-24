package com.olt.racketclash.state.team

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.list.ListModel

class TeamTableModel(
    private val database: TeamDatabase,
    private val tournamentId: Long
) : ListModel<Team, TeamFilter, TeamSorting>(
    emptyItem = Team(),
    initialFilter = TeamFilter(tournamentId = tournamentId),
    initialSorting = TeamSorting.NameAsc
) {
    override suspend fun databaseDelete(item: Team) =
        database.delete(id = item.id)

    override suspend fun databaseSelect(
        filter: TeamFilter,
        sorting: TeamSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Team, TeamFilter, TeamSorting> =
        database.selectList(
            filter = filter,
            sorting = sorting,
            fromIndex = fromIndex,
            toIndex = toIndex
        )

    override suspend fun databaseAdd(item: Team) =
        database.add(team = item.copy(tournamentId = tournamentId))
}