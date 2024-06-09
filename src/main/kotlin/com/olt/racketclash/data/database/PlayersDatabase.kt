package com.olt.racketclash.data.database

import com.olt.racketclash.data.Player
import com.olt.racketclash.database.PlayerQueries
import com.olt.racketclash.data.database.mapper.toPlayer
import kotlinx.coroutines.flow.Flow

class PlayersDatabase(private val queries: PlayerQueries) {

    fun players(): Flow<List<Player>> =
        queries
            .selectAll()
            .mapToList { it.toPlayer() }

    fun player(id: Long): Flow<Player?> =
        queries
            .select(id = id)
            .mapToSingle { it?.toPlayer() }

    fun activePlayers(): Flow<List<Player>> =
        queries
            .selectActive()
            .mapToList { it.toPlayer() }

    private fun size(): Int =
        queries
            .size()
            .executeAsOneOrNull()
            ?.toInt() ?: 0

    fun addPlayer(name: String, teamId: Long): Int {
        queries.add(name = name, teamId = teamId)
        return size()
    }

    fun updatePlayer(id: Long, name: String, teamId: Long) = queries.update(id = id, name = name, teamId = teamId)

    fun setActive(id: Long, active: Boolean) = queries.setActive(id = id, active = active)

    fun deletePlayer(id: Long): Int {
        queries.delete(id = id)
        return size()
    }

    fun setLastPlayed(id: Long?, lastPlayed: Long?) = id?.let { queries.setLastPlayed(id = it, lastPLayed = lastPlayed) }
}