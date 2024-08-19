package com.olt.racketclash.data.database.bye

import com.olt.racketclash.data.Bye
import com.olt.racketclash.data.database.IByeDatabase
import com.olt.racketclash.data.database.TimeStamp
import com.olt.racketclash.data.database.mapToList
import com.olt.racketclash.database.RacketClashDatabase
import kotlinx.coroutines.flow.Flow

internal class ByeDatabase(
    private val database: RacketClashDatabase
): TimeStamp(), IByeDatabase {

    override fun byes(): Flow<List<Bye>> =
        database
            .byeQueries
            .selectAll()
            .mapToList { it.toBye() }

    override fun byes(roundId: Long): Flow<List<Bye>> =
        database
            .byeQueries
            .selectAllInRound(roundId = roundId)
            .mapToList { it.toBye() }

    override fun add(roundId: Long, playerId: Long, projectId: Long) =
        database.transaction {
            database.byeQueries.add(roundId = roundId, playerId = playerId)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun delete(id: Long, projectId: Long) =
        database.transaction {
            database.byeQueries.delete(id = id)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

}