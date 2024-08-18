package com.olt.racketclash.data.database

import com.olt.racketclash.data.Project
import com.olt.racketclash.data.ProjectSettings
import kotlinx.coroutines.flow.Flow

interface IProjectDatabase {
    fun projects(): Flow<List<Project>>
    fun projectSettings(id: Long): Flow<ProjectSettings?>
    fun addProject(name: String)
    fun deleteProject(id: Long)
    fun updateLastModified(id: Long)
    fun updateFields(id: Long, fields: Int)
    fun updateTimeout(id: Long, timeout: Int)
    fun updateGamePointsForBye(id: Long, gamePointsForBye: Int)
    fun updateSetPointsForBye(id: Long, setPointsForBye: Int)
    fun updatePointsForBye(id: Long, pointsForBye: Int)
}