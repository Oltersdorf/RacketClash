package com.olt.racketclash.data.database.player

import com.olt.racketclash.data.Player
import com.olt.racketclash.data.database.IPlayerDatabase
import com.olt.racketclash.data.database.TimeStamp
import com.olt.racketclash.data.database.mapToList
import com.olt.racketclash.data.database.mapToSingle
import com.olt.racketclash.database.RacketClashDatabase
import kotlinx.coroutines.flow.Flow

class PlayerDatabase(
    private val database: RacketClashDatabase
): TimeStamp(), IPlayerDatabase {

    override fun players(): Flow<List<Player>> =
        database
            .playerQueries
            .selectAll()
            .mapToList { it.toPlayer() }

    override fun player(id: Long): Flow<Player?> =
        database
            .playerQueries
            .select(id = id)
            .mapToSingle { it?.toPlayer() }

    override fun activePlayers(): Flow<List<Player>> =
        database
            .playerQueries
            .selectActive()
            .mapToList { it.toPlayer() }

    override fun add(name: String, teamId: Long, projectId: Long) =
        database.transaction {
            database.playerQueries.add(name = name, teamId = teamId, projectId = projectId)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun update(id: Long, name: String, teamId: Long, projectId: Long) =
        database.transaction {
            database.playerQueries.update(id = id, name = name, teamId = teamId)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun setActive(id: Long, active: Boolean, projectId: Long) =
        database.transaction {
            database.playerQueries.setActive(id = id, active = active)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun delete(id: Long, projectId: Long) =
        database.transaction {
            database.playerQueries.delete(id = id)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }
}