package com.olt.racketclash.database.impl

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.toName
import com.olt.racketclash.database.toTeam

internal class TeamDatabaseImpl(
    private val database: RacketClashDatabase
) : TeamDatabase {

    override suspend fun selectList(
        filter: TeamFilter,
        sorting: TeamSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Team, TeamFilter, TeamSorting> =
        database.transactionWithResult {
            FilteredSortedList(
                totalSize = database
                    .teamQueries
                    .selectFilteredAndOrderedSize(
                        tournamentId = filter.tournamentId,
                        nameFilter = filter.name
                    ).executeAsOne(),
                fromIndex = fromIndex,
                toIndex = toIndex,
                items = database
                    .teamQueries
                    .filteredAndOrderedTeam(
                        tournamentId = filter.tournamentId,
                        nameFilter = filter.name,
                        sorting = sorting.toName(),
                        limit = toIndex,
                        offset = fromIndex
                    ).executeAsList()
                    .map { it.toTeam() },
                filter = filter,
                sorting = sorting
            )
        }

    override suspend fun selectLast(n: Long): List<Team> =
        database
            .teamQueries
            .selectLast(n = n)
            .executeAsList()
            .map { it.toTeam() }

    override suspend fun selectSingle(id: Long): Team =
        database
            .teamQueries
            .team(id = id)
            .executeAsOne()
            .toTeam()

    override suspend fun add(team: Team, playerIds: Set<Long>) =
        database.transaction {
            database.teamQueries.add(name = team.name, rank = team.rank, tournamentId = team.tournamentId)
            val teamId = database.teamQueries.lastInsertedId().executeAsOne()
            playerIds.forEach {
                database.playerToTeamQueries.add(playerId = it, teamId = teamId)
            }
        }

    override suspend fun update(team: Team, playerIds: Set<Long>) =
        database.transaction {
            database.teamQueries.update(
                id = team.id,
                name = team.name,
                rank = team.rank
            )
            database.playerToTeamQueries.deleteByTeam(teamId = team.id)
            playerIds.forEach {
                database.playerToTeamQueries.add(playerId = it, teamId = team.id)
            }
        }

    override suspend fun delete(id: Long) =
        database.teamQueries.delete(id = id)
}