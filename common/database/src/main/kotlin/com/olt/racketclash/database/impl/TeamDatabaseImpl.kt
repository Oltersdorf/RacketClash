package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.FilteredSortedList
import com.olt.racketclash.database.api.Team
import com.olt.racketclash.database.api.TeamDatabase
import com.olt.racketclash.database.api.TeamFilter
import com.olt.racketclash.database.api.TeamSorting
import kotlin.math.min

internal class TeamDatabaseImpl : TeamDatabase {
    private val teams = mutableListOf<Team>()

    override suspend fun selectList(
        filter: TeamFilter,
        sorting: TeamSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Team, TeamFilter, TeamSorting> =
        FilteredSortedList(
            totalSize = teams.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = teams.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), teams.size)),
            filter = filter,
            sorting = sorting
        )

    override suspend fun selectLast(n: Long): List<Team> =
        teams.takeLast(n.toInt())

    override suspend fun selectSingle(id: Long): Team =
        teams.first { it.id == id }

    override suspend fun add(team: Team) {
        teams.add(team)
    }

    override suspend fun update(team: Team) {
        teams.replaceAll {
            if (it.id == team.id)
                team
            else
                it
        }
    }

    override suspend fun delete(id: Long) {
        teams.removeIf { it.id == id }
    }
}