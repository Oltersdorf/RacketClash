package com.olt.racketclash.database.impl

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.toName
import com.olt.racketclash.database.toPlayer

internal class PlayerDatabaseImpl(
    private val database: RacketClashDatabase
): PlayerDatabase {

    override suspend fun selectList(
        filter: PlayerFilter,
        sorting: PlayerSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Player, PlayerFilter, PlayerSorting> =
        database.transactionWithResult {
            FilteredSortedList(
                totalSize = database
                    .playerQueries
                    .selectFilteredAndOrderedSize(
                        nameFilter = filter.name,
                        clubFilter = filter.club,
                        birthYearMin = filter.birthYear.first,
                        birthYearMax = filter.birthYear.last,
                        medalsMin = filter.medals.first,
                        medalsMax = filter.medals.last
                    ).executeAsOne().size,
                fromIndex = fromIndex,
                toIndex = toIndex,
                items = database
                    .playerQueries
                    .filteredAndOrderedPlayer(
                        nameFilter = filter.name,
                        clubFilter = filter.club,
                        birthYearMin = filter.birthYear.first,
                        birthYearMax = filter.birthYear.last,
                        medalsMin = filter.medals.first,
                        medalsMax = filter.medals.last,
                        sorting = sorting.toName(),
                        offset = fromIndex,
                        limit = toIndex
                    ).executeAsList()
                    .map { it.toPlayer() },
                filter = filter,
                sorting = sorting
            )
        }

    override suspend fun selectLast(n: Long): List<Player> =
        database
            .playerQueries
            .selectLast(n = n)
            .executeAsList()
            .map { it.toPlayer() }

    override suspend fun selectSingle(id: Long): Player =
        database
            .playerQueries
            .player(id = id)
            .executeAsOne()
            .toPlayer()

    override suspend fun clubs(filter: String) =
        database
            .playerQueries
            .clubs(filter = filter)
            .executeAsList()

    override suspend fun add(player: Player) =
        database.playerQueries.add(
            name = player.name,
            birthYear = player.birthYear,
            club = player.club
        )

    override suspend fun update(player: Player) =
        database.playerQueries.update(
            id = player.id,
            name = player.name,
            birthYear = player.birthYear,
            club = player.club
        )

    override suspend fun delete(id: Long) =
        database.playerQueries.delete(id = id)
}