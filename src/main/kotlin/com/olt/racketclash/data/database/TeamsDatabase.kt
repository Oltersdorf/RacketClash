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

    private fun size(): Int =
        queries
            .size()
            .executeAsOneOrNull()
            ?.toInt() ?: 0

    fun addTeam(name: String, strength: Int): Int {
        queries.add(name = name, strength = strength)
        return size()
    }

    fun updateTeam(id: Long, name: String, strength: Int) = queries.update(id = id, name = name, strength = strength)

    fun deleteTeam(id: Long): Int {
        queries.delete(id = id)
        return size()
    }
}