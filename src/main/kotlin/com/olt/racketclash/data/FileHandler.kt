package com.olt.racketclash.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories

class FileHandler {
    private val racketClashPath: Path = Path(System.getProperty("user.home"), ".racketClash")
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    private val projectsChannel = MutableStateFlow<List<Project>>(emptyList())
    private val fieldsChannel = MutableStateFlow(1)
    private val timeoutChannel = MutableStateFlow(1)

    companion object {
        val defaultProjectLocation: String = Path(System.getProperty("user.home")).absolutePathString()
    }

    init {
        runBlocking(Dispatchers.IO) {
            racketClashPath.createDirectories()
            loadProjects()
        }
    }

    fun projects() : StateFlow<List<Project>> = projectsChannel.asStateFlow()
    fun fields() : StateFlow<Int> = fieldsChannel.asStateFlow()
    fun timeout() : StateFlow<Int> = timeoutChannel.asStateFlow()

    private fun currentTime(): String = LocalDateTime.now().format(dateTimeFormatter)

    fun setCurrentProject(project: Project) {
        runBlocking(Dispatchers.IO) {
            fieldsChannel.emit(project.fields)
            timeoutChannel.emit(project.timeout)
        }
    }

    suspend fun addProject(name: String, location: String) {
        val projectPath = Path(location, name)
        projectPath.createDirectories()
        val newProjects = listOf(
            Project(name = name, lastModified = currentTime(), location = projectPath.absolutePathString()),
            *projectsChannel.value.toTypedArray()
        ).sortedBy { it.lastModified }

        writeProjects(projects = newProjects)

        projectsChannel.emit(newProjects)
    }

    suspend fun deleteProject(name: String) {
        val project = projectsChannel.value.find { it.name == name }

        if (project != null) {
            val newProjects = projectsChannel.value.toMutableList()
            newProjects.remove(project)
            writeProjects(newProjects.toList())
            projectsChannel.emit(newProjects)
        }
    }

    private suspend fun loadProjects() {
        val projectsFile = File(racketClashPath.absolutePathString(), "Projects.json")

        if (projectsFile.exists()) {
            val jsonString = projectsFile.readText()
            val projects: List<Project> = Json.decodeFromString<List<Project>>(jsonString)
            projectsChannel.emit(projects)
        }
    }

    suspend fun updatePlayerCountForProject(projectName: String, playerNumber: Int) {
        val projects = projectsChannel.value.toMutableList()
        projects.replaceAll { if (it.name == projectName) it.copy(playerNumber = playerNumber, lastModified = currentTime()) else it }
        writeProjects(projects)
        projectsChannel.emit(projects)
    }

    suspend fun updateTeamCountForProject(projectName: String, teamNumber: Int) {
        val projects = projectsChannel.value.toMutableList()
        projects.replaceAll { if (it.name == projectName) it.copy(teamNumber = teamNumber, lastModified = currentTime()) else it }
        writeProjects(projects)
        projectsChannel.emit(projects)
    }

    suspend fun setFields(newFields: Int) {
        fieldsChannel.emit(newFields)
        writeProjects(projects = projectsChannel.value)
    }

    suspend fun setTimeout(newTimeout: Int) {
        timeoutChannel.emit(newTimeout)
        writeProjects(projects = projectsChannel.value)
    }

    private fun writeProjects(projects: List<Project>) {
        val jsonString = Json.encodeToString(projects)
        File(racketClashPath.absolutePathString(), "Projects.json").writeText(jsonString)
    }
}