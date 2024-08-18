package com.olt.racketclash.data.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.data.Project
import com.olt.racketclash.data.ProjectSettings
import com.olt.racketclash.data.database.mapper.toProject
import com.olt.racketclash.data.database.mapper.toProjectSettings
import com.olt.racketclash.database.ProjectQueries
import com.olt.racketclash.database.ProjectTable
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ProjectsDatabase(private val queries: ProjectQueries) {

    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    companion object {
        val projectAdapter by lazy {
            ProjectTable.Adapter(
                fieldsAdapter = IntColumnAdapter,
                timeoutAdapter = IntColumnAdapter,
                gamePointsForByeAdapter = IntColumnAdapter,
                setPointsForByeAdapter = IntColumnAdapter,
                pointsForByeAdapter = IntColumnAdapter
            )
        }
    }

    private fun currentTime(): String = LocalDateTime.now().format(dateTimeFormatter)

    fun projects(): Flow<List<Project>> =
        queries
            .selectAll()
            .mapToList { it.toProject() }

    fun projectSettings(id: Long): Flow<ProjectSettings?> =
        queries
            .selectSettings(id = id)
            .mapToSingle { it?.toProjectSettings() }

    fun addProject(name: String) =
        queries.add(name = name, lastModified = currentTime())

    fun deleteProject(id: Long) =
        queries.delete(id = id)

    fun updateLastModified(id: Long) =
        queries.updateLastModified(id = id, lastModified = currentTime())

    fun updateFields(id: Long, fields: Int) =
        queries.updateFields(id = id, fields = fields)

    fun updateTimeout(id: Long, timeout: Int) =
        queries.updateTimeout(id = id, timeout = timeout)

    fun updateGamePointsForBye(id: Long, gamePointsForBye: Int) =
        queries.updateGamePointsForBye(id = id, gamePointsForBye = gamePointsForBye)

    fun updateSetPointsForBye(id: Long, setPointsForBye: Int) =
        queries.updateSetPointsForBye(id = id, setPointsForBye = setPointsForBye)

    fun updatePointsForBye(id: Long, pointsForBye: Int) =
        queries.updatePointsForBye(id = id, pointsForBye = pointsForBye)
}