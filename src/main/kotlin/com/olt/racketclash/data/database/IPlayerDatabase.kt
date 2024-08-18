package com.olt.racketclash.data.database

import com.olt.racketclash.data.Player
import kotlinx.coroutines.flow.Flow

interface IPlayerDatabase {
    fun players(): Flow<List<Player>>
    fun player(id: Long): Flow<Player?>
    fun activePlayers(): Flow<List<Player>>
    fun add(name: String, teamId: Long, projectId: Long)
    fun update(id: Long, name: String, teamId: Long, projectId: Long)
    fun setActive(id: Long, active: Boolean, projectId: Long)
    fun delete(id: Long, projectId: Long)
}