package com.olt.racketclash.model

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.olt.racketclash.data.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

class ProjectModal : StateScreenModel<ProjectModal.Modal>(Modal()) {

    private val racketClashPath = Path(System.getProperty("user.home"), ".racketClash")
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            racketClashPath.createDirectories()
            loadProjects()
        }
    }

    data class Modal(
        val projects: List<Project> = emptyList()
    )

    fun addProject(name: String, location: Path) {
        screenModelScope.launch(context = Dispatchers.IO) {
            val projectPath = Path(location.absolutePathString(), name)
            projectPath.createDirectories()
            val newProjects = listOf(
                Project(name = name, lastModified = LocalDateTime.now().format(dateTimeFormatter), location = projectPath.absolutePathString()),
                *mutableState.value.projects.toTypedArray()
            ).sortedBy { it.lastModified }

           writeProjects(projects = newProjects)

            mutableState.value = Modal(projects = newProjects)
        }
    }

    fun deleteProject(name: String) {
        screenModelScope.launch(context = Dispatchers.IO) {
            val project = mutableState.value.projects.find { it.name == name }

            if (project != null) {
                val newProjects = mutableState.value.projects.toMutableList()
                newProjects.remove(project)
                writeProjects(newProjects.toList())
                mutableState.value = Modal(projects = newProjects)
            }
        }
    }

    fun updateProject(name: String, teamNumber: Int, playerNumber: Int) {
        screenModelScope.launch(context = Dispatchers.IO) {
            val project = mutableState.value.projects.find { it.name == name }

            if (project != null) {
                val newProjects = mutableState.value.projects.toMutableList()
                val projectIndex = newProjects.indexOf(project)
                newProjects[projectIndex] = project.copy(
                    teamNumber = teamNumber,
                    playerNumber = playerNumber
                )
                writeProjects(newProjects.toList())
                mutableState.value = Modal(projects = newProjects)
            }
        }
    }

    private fun loadProjects() {
        val projectsFile = File(racketClashPath.absolutePathString(), "Projects.json")

        if (projectsFile.exists()) {
            val jsonString = projectsFile.readText()
            val projects = Json.decodeFromString<List<Project>>(jsonString)
            mutableState.value = Modal(projects = projects)
        }
    }

    private fun writeProjects(projects: List<Project>) {
        val jsonString = Json.encodeToString(projects)
        File(racketClashPath.absolutePathString(), "Projects.json").writeText(jsonString)
    }
}