package com.olt.racketclash.data.database

import com.olt.racketclash.data.Bye
import kotlinx.coroutines.flow.Flow

interface IByeDatabase {
    fun byes(): Flow<List<Bye>>
    fun byes(roundId: Long): Flow<List<Bye>>
    fun add(roundId: Long, playerId: Long, projectId: Long)
    fun delete(id: Long, projectId: Long)
}