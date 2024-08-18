package com.olt.racketclash.data.database.round

import com.olt.racketclash.data.Round
import com.olt.racketclash.data.database.IRoundDatabase
import com.olt.racketclash.data.database.TimeStamp
import com.olt.racketclash.data.database.mapToList
import com.olt.racketclash.data.database.mapToSingle
import com.olt.racketclash.database.RacketClashDatabase
import kotlinx.coroutines.flow.Flow

class RoundDatabase(
    private val database: RacketClashDatabase
): TimeStamp(), IRoundDatabase {

    override fun rounds(): Flow<List<Round>> =
        database
            .roundQueries
            .selectAll()
            .mapToList { it.toRound() }

    override fun round(id: Long): Flow<Round?> =
        database
            .roundQueries
            .select(id = id)
            .mapToSingle { it?.toRound() }

    override fun add(name: String, projectId: Long) =
        database.transaction {
            database.roundQueries.add(name = name)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun delete(id: Long, projectId: Long) =
        database.transaction {
            database.roundQueries.delete(id = id)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun updateName(id: Long, name: String, projectId: Long) =
        database.transaction {
            database.roundQueries.updateName(id = id, name = name)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }
}