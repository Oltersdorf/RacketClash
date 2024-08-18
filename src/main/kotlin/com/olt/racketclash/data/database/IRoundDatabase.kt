package com.olt.racketclash.data.database

import com.olt.racketclash.data.Round
import kotlinx.coroutines.flow.Flow

interface IRoundDatabase {
    fun rounds(): Flow<List<Round>>
    fun round(id: Long): Flow<Round?>
    fun add(name: String, projectId: Long)
    fun delete(id: Long, projectId: Long)
    fun updateName(id: Long, name: String, projectId: Long)
}