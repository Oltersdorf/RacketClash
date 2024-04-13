package com.olt.racketclash.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
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

    var currentProject : Project? = null
        private set

    var selectedLanguage: String
        private set

    companion object {
        val defaultProjectLocation: String = Path(System.getProperty("user.home")).absolutePathString()
    }

    init {
        racketClashPath.createDirectories()
        val settings = loadSettings()
        selectedLanguage = settings.language

        runBlocking(Dispatchers.IO) {
            projectsChannel.emit(settings.projects)
        }
    }

    @Serializable
    private data class RacketClashSettings(
        val language: String,
        val projects: List<Project>
    )

    fun projects() : StateFlow<List<Project>> = projectsChannel.asStateFlow()

    private fun currentTime(): String = LocalDateTime.now().format(dateTimeFormatter)

    fun setCurrentProject(project: Project) {
        currentProject = project
    }

    fun setLanguage(language: String) {
        selectedLanguage = language
        writeSettings(settings = RacketClashSettings(language = language, projects = projectsChannel.value))
    }

    suspend fun addProject(name: String, location: String) {
        val projectPath = Path(location, name)
        projectPath.createDirectories()
        val newProjects = listOf(
            Project(name = name, lastModified = currentTime(), location = projectPath.absolutePathString()),
            *projectsChannel.value.toTypedArray()
        ).sortedBy { it.lastModified }

        writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = newProjects))

        projectsChannel.emit(newProjects)
    }

    suspend fun deleteProject(name: String) {
        val project = projectsChannel.value.find { it.name == name }

        if (project != null) {
            val newProjects = projectsChannel.value.toMutableList()
            newProjects.remove(project)
            writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = newProjects.toList()))
            projectsChannel.emit(newProjects)
        }
    }

    private fun loadSettings(): RacketClashSettings {
        val projectsFile = File(racketClashPath.absolutePathString(), "Settings.json")

        if (projectsFile.exists()) {
            val jsonString = projectsFile.readText()
            return Json.decodeFromString<RacketClashSettings>(jsonString)
        }

        return RacketClashSettings(language = "English", projects = emptyList())
    }

    suspend fun updatePlayerCount(playerNumber: Int) {
        val projects = projectsChannel.value.toMutableList()
        projects.replaceAll { if (it.name == currentProject?.name) it.copy(playerNumber = playerNumber, lastModified = currentTime()) else it }
        projectsChannel.emit(projects)
        writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = projects))
    }

    suspend fun updateTeamCount(teamNumber: Int) {
        val projects = projectsChannel.value.toMutableList()
        projects.replaceAll { if (it.name == currentProject?.name) it.copy(teamNumber = teamNumber, lastModified = currentTime()) else it }
        projectsChannel.emit(projects)
        writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = projects))
    }

    suspend fun setFields(newFields: Int) {
        currentProject = currentProject?.copy(fields = newFields)
        val newProjects = projectsChannel.value.toMutableList()
        newProjects.replaceAll { if (it.name == currentProject?.name) it.copy(fields = newFields) else it }
        projectsChannel.emit(newProjects)
        writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = newProjects))
    }

    suspend fun setTimeout(newTimeout: Int) {
        currentProject = currentProject?.copy(timeout = newTimeout)
        val newProjects = projectsChannel.value.toMutableList()
        newProjects.replaceAll { if (it.name == currentProject?.name) it.copy(timeout = newTimeout) else it }
        projectsChannel.emit(newProjects)
        writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = newProjects))
    }

    suspend fun setGamePointsForBye(newGamePointsForBye: Int) {
        currentProject = currentProject?.copy(gamePointsForBye = newGamePointsForBye)
        val newProjects = projectsChannel.value.toMutableList()
        newProjects.replaceAll { if (it.name == currentProject?.name) it.copy(gamePointsForBye = newGamePointsForBye) else it }
        projectsChannel.emit(newProjects)
        writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = newProjects))
    }

    suspend fun setSetPointsForBye(newSetPointsForBye: Int) {
        currentProject = currentProject?.copy(setPointsForBye = newSetPointsForBye)
        val newProjects = projectsChannel.value.toMutableList()
        newProjects.replaceAll { if (it.name == currentProject?.name) it.copy(setPointsForBye = newSetPointsForBye) else it }
        projectsChannel.emit(newProjects)
        writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = newProjects))
    }

    suspend fun setPointsForBye(newPointsForBye: Int) {
        currentProject = currentProject?.copy(pointsForBye = newPointsForBye)
        val newProjects = projectsChannel.value.toMutableList()
        newProjects.replaceAll { if (it.name == currentProject?.name) it.copy(pointsForBye = newPointsForBye) else it }
        projectsChannel.emit(newProjects)
        writeSettings(settings = RacketClashSettings(language = selectedLanguage, projects = newProjects))
    }

    private fun writeSettings(settings: RacketClashSettings) {
        val jsonString = Json.encodeToString(settings)
        File(racketClashPath.absolutePathString(), "Settings.json").writeText(jsonString)
    }
}