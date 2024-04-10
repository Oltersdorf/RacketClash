package com.olt.racketclash.database

import com.olt.racketclash.data.Bye
import com.olt.racketclash.database.mapper.toBye
import kotlinx.coroutines.flow.Flow

class ByeDatabase(private val queries: ByeQueries) {

    fun byes(): Flow<List<Bye>> =
        queries
            .selectAll()
            .mapToList { it.toBye() }

    fun byes(roundId: Long): Flow<List<Bye>> =
        queries
            .selectAllInRound(roundId = roundId)
            .mapToList { it.toBye() }

    fun add(roundId: Long?, playerId: Long) = roundId?.let { queries.add(roundId = it, playerId = playerId) }

    fun delete(id: Long) = queries.delete(id = id)
}