package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.FilteredSortedList
import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.database.api.PlayerFilter
import com.olt.racketclash.database.api.PlayerSorting
import kotlin.math.min

internal class PlayerDatabaseImpl: PlayerDatabase {
    private val players = mutableListOf<Player>()

    override suspend fun selectList(
        filter: PlayerFilter,
        sorting: PlayerSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Player, PlayerFilter, PlayerSorting> =
        FilteredSortedList(
            totalSize = players.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = players.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), players.size)),
            filter = filter,
            sorting = sorting
        )

    override suspend fun selectLast(n: Long): List<Player> =
        players.takeLast(n.toInt())

    override suspend fun selectSingle(id: Long): Player =
        players.first { it.id == id }

    override suspend fun clubs(filter: String): List<String> =
        players.map { it.club }.toSet().toList()

    override suspend fun add(player: Player) {
        players.add(player)
    }

    override suspend fun update(player: Player) {
        players.replaceAll {
            if (it.id == player.id)
                player
            else
                it
        }
    }

    override suspend fun delete(id: Long) {
        players.removeIf { it.id == id }
    }
}