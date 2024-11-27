package com.olt.racketclash.database.team

import com.olt.racketclash.database.RacketClashDatabase

class TeamDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrdered(
        tournamentId: Long,
        nameFilter: String,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<DeletableTeam>> =
        database.teamQueries.selectFilteredAndOrderedSize(
            tournamentId = tournamentId,
            nameFilter = nameFilter
        ).executeAsOne() to
        database.teamQueries.selectFilteredAndOrdered(
            tournamentId = tournamentId,
            nameFilter = nameFilter,
            sorting = sorting.name,
            limit = toIndex.toLong(),
            offset = fromIndex.toLong()
        ).executeAsList().map { it.toDeletableTeam() }

    fun selectSingle(id: Long): DeletableTeam =
        database.teamQueries
            .selectSingle(id = id)
            .executeAsOne()
            .toDeletableTeam()

    fun add(team: DeletableTeam, tournamentId: Long) =
        database.teamQueries.add(name = team.name, tournamentId = tournamentId)

    fun update(team: DeletableTeam) =
        database.teamQueries.update(
            id = team.id,
            name = team.name
        )

    fun delete(id: Long) =
        database.teamQueries.delete(id = id)
}