package com.olt.racketclash.data.database

import com.olt.racketclash.data.Team
import kotlinx.coroutines.flow.Flow

interface ITeamDatabase {
    fun teams(): Flow<List<Team>>
    fun team(id: Long): Flow<Team?>
    fun add(name: String, strength: Int, projectId: Long)
    fun update(id: Long, name: String, strength: Int, projectId: Long)
    fun delete(id: Long, projectId: Long)
}