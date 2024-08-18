package com.olt.racketclash.data.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.data.Team
import com.olt.racketclash.database.TeamQueries
import com.olt.racketclash.database.TeamTable
import com.olt.racketclash.data.database.mapper.toTeam
import kotlinx.coroutines.flow.Flow

class TeamsDatabase(private val queries: TeamQueries) {

    companion object {
        val teamAdapter by lazy { TeamTable.Adapter(strengthAdapter = IntColumnAdapter) }
    }

    fun teams(): Flow<List<Team>> =
        queries
            .selectAll()
            .mapToList { it.toTeam() }

    fun team(id: Long): Flow<Team?> =
        queries
            .select(id = id)
            .mapToSingle { it?.toTeam() }

    fun addTeam(name: String, strength: Int, projectId: Long) =
        queries.add(name = name, strength = strength, projectId = projectId)

    fun updateTeam(id: Long, name: String, strength: Int) =
        queries.update(id = id, name = name, strength = strength)

    fun deleteTeam(id: Long) =
        queries.delete(id = id)
}