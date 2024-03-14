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

    private val projectsChannel = MutableSharedFlow<List<Project>>(replay = 1)

    companion object {
        val defaultProjectLocation: String = Path(System.getProperty("user.home")).absolutePathString()
    }

    init {
        runBlocking(Dispatchers.IO) {
            racketClashPath.createDirectories()
            loadProjects()
        }
    }

    fun projects() : SharedFlow<List<Project>> = projectsChannel.asSharedFlow()

    suspend fun addProject(name: String, location: String) {
        val projectPath = Path(location, name)
        projectPath.createDirectories()
        val newProjects = listOf(
            Project(name = name, lastModified = LocalDateTime.now().format(dateTimeFormatter), location = projectPath.absolutePathString()),
            *projectsChannel.last().toTypedArray()
        ).sortedBy { it.lastModified }

        writeProjects(projects = newProjects)

        projectsChannel.emit(newProjects)
    }

    suspend fun deleteProject(name: String) {
        val project = projectsChannel.last().find { it.name == name }

        if (project != null) {
            val newProjects = projectsChannel.last().toMutableList()
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

    private fun writeProjects(projects: List<Project>) {
        val jsonString = Json.encodeToString(projects)
        File(racketClashPath.absolutePathString(), "Projects.json").writeText(jsonString)
    }
}