package com.olt.racketclash.data.database.project

import com.olt.racketclash.data.Project
import com.olt.racketclash.data.ProjectSettings
import com.olt.racketclash.data.database.IProjectDatabase
import com.olt.racketclash.data.database.TimeStamp
import com.olt.racketclash.data.database.mapToList
import com.olt.racketclash.data.database.mapToSingle
import com.olt.racketclash.database.RacketClashDatabase
import kotlinx.coroutines.flow.Flow

class ProjectDatabase(
    private val database: RacketClashDatabase
) : TimeStamp(), IProjectDatabase {

    override fun projects(): Flow<List<Project>> =
        database
            .projectQueries
            .selectAll()
            .mapToList { it.toProject() }

    override fun projectSettings(id: Long): Flow<ProjectSettings?> =
        database
            .projectQueries
            .selectSettings(id = id)
            .mapToSingle { it?.toProjectSettings() }

    override fun addProject(name: String) =
        database.projectQueries.add(name = name, lastModified = currentTime())

    override fun deleteProject(id: Long) =
        database.projectQueries.delete(id = id)

    override fun updateLastModified(id: Long) =
        database.projectQueries.updateLastModified(id = id, lastModified = currentTime())

    override fun updateFields(id: Long, fields: Int) =
        database.projectQueries.updateFields(id = id, fields = fields)

    override fun updateTimeout(id: Long, timeout: Int) =
        database.projectQueries.updateTimeout(id = id, timeout = timeout)

    override fun updateGamePointsForBye(id: Long, gamePointsForBye: Int) =
        database.projectQueries.updateGamePointsForBye(id = id, gamePointsForBye = gamePointsForBye)

    override fun updateSetPointsForBye(id: Long, setPointsForBye: Int) =
        database.projectQueries.updateSetPointsForBye(id = id, setPointsForBye = setPointsForBye)

    override fun updatePointsForBye(id: Long, pointsForBye: Int) =
        database.projectQueries.updatePointsForBye(id = id, pointsForBye = pointsForBye)
}