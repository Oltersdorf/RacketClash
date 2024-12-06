package com.olt.racketclash.database.team

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.table.FilteredAndOrderedTeam
import com.olt.racketclash.database.table.Team

class TeamDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrdered(
        tournamentId: Long,
        nameFilter: String,
        rankFilter: Int?,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<FilteredAndOrderedTeam>> =
        database.teamQueries.selectFilteredAndOrderedSize(
            tournamentId = tournamentId,
            nameFilter = nameFilter,
            rankFilter = rankFilter
        ).executeAsOne() to
        database.teamQueries.filteredAndOrderedTeam(
            tournamentId = tournamentId,
            nameFilter = nameFilter,
            rankFilter = rankFilter,
            sorting = sorting.name,
            limit = toIndex.toLong(),
            offset = fromIndex.toLong()
        ).executeAsList()

    fun selectSingle(id: Long): Team =
        database.teamQueries
            .team(id = id)
            .executeAsOne()

    fun add(team: Team, tournamentId: Long) =
        database.teamQueries.add(name = team.name, rank = team.rank, tournamentId = tournamentId)

    fun update(team: Team) =
        database.teamQueries.update(
            id = team.id,
            name = team.name,
            rank = team.rank
        )

    fun delete(id: Long) =
        database.teamQueries.delete(id = id)
}