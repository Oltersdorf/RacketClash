package com.olt.racketclash.data.database.team

import com.olt.racketclash.data.Team
import com.olt.racketclash.data.database.ITeamDatabase
import com.olt.racketclash.data.database.TimeStamp
import com.olt.racketclash.data.database.mapToList
import com.olt.racketclash.data.database.mapToSingle
import com.olt.racketclash.database.RacketClashDatabase
import kotlinx.coroutines.flow.Flow

internal class TeamDatabase(
    private val database: RacketClashDatabase
): TimeStamp(), ITeamDatabase {

    override fun teams(): Flow<List<Team>> =
        database
            .teamQueries
            .selectAll()
            .mapToList { it.toTeam() }

    override fun team(id: Long): Flow<Team?> =
        database
            .teamQueries
            .select(id = id)
            .mapToSingle { it?.toTeam() }

    override fun add(name: String, strength: Int, projectId: Long) =
        database.transaction {
            database.teamQueries.add(name = name, strength = strength, projectId = projectId)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun update(id: Long, name: String, strength: Int, projectId: Long) =
        database.transaction {
            database.teamQueries.update(id = id, name = name, strength = strength)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun delete(id: Long, projectId: Long) =
        database.transaction {
            database.teamQueries.delete(id = id)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }
}