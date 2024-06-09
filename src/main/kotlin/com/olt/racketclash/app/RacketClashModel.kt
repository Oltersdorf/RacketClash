package com.olt.racketclash.app

import com.olt.racketclash.data.Project
import com.olt.racketclash.data.database.Database
import com.olt.racketclash.language.English
import com.olt.racketclash.language.German
import com.olt.racketclash.language.Language
import com.olt.racketclash.state.ViewModelState
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

class RacketClashModel : ViewModelState<RacketClashModel.State>(State()) {
    private val racketClashPath: Path = Path(System.getProperty("user.home"), ".racketClash")
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    data class State(
        val availableLanguages: Set<String> = setOf("English", "Deutsch"),
        val language: Language = English,
        val projects: List<Project> = emptyList(),
        val currentProject: Project? = null,
        val database: Database? = null
    )

    @Serializable
    private data class RacketClashSettings(
        val language: String,
        val projects: List<Project>
    )

    companion object {
        val defaultProjectLocation: String = Path(System.getProperty("user.home")).absolutePathString()
    }

    init {
        loadSettings()
    }

    private fun currentTime(): String = LocalDateTime.now().format(dateTimeFormatter)

    private fun getLanguageFromString(language: String) =
        when (language) {
            English.languageName -> English
            German.languageName -> German
            else -> English
        }

    private fun loadSettings() =
        onIO {
            racketClashPath.createDirectories()
            val projectsFile = File(racketClashPath.absolutePathString(), "Settings.json")

            if (projectsFile.exists()) {
                val jsonString = projectsFile.readText()
                val settings = Json.decodeFromString<RacketClashSettings>(jsonString)

                val language = getLanguageFromString(language = settings.language)

                updateState {
                    copy(
                        projects = settings.projects,
                        language = language
                    )
                }
            } else
                updateState {
                    copy(
                        projects = emptyList(),
                        language = English
                    )
                }
        }

    private fun writeSettings() =
        onIO {
            val settings = RacketClashSettings(
                language = state.value.language.languageName,
                projects = state.value.projects
            )

            val jsonString = Json.encodeToString(settings)
            File(racketClashPath.absolutePathString(), "Settings.json").writeText(jsonString)
        }

    fun setCurrentProject(project: Project?) =
        onMain {
            updateState {
                copy(
                    currentProject = project,
                    database = if (project != null)
                        Database(tournamentPath = project.location, appModel = this@RacketClashModel)
                    else null
                )
            }
        }

    fun setLanguage(language: String) =
        onDefault {
            val lang = getLanguageFromString(language = language)

            updateState { copy(language = lang) }
            writeSettings()
        }

    fun addProject(name: String, location: String) =
        onDefault {
            val projectPath = Path(location, name)
            projectPath.createDirectories()
            val newProjects = listOf(
                Project(name = name, lastModified = currentTime(), location = projectPath.absolutePathString()),
                *state.value.projects.toTypedArray()
            ).sortedBy { it.lastModified }

            updateState { copy(projects = newProjects) }

            writeSettings()
        }

    fun deleteProject(project: Project) =
        onDefault {
            val newProjects = state.value.projects.toMutableList()
            newProjects.remove(project)

            updateState { copy(projects = newProjects.toList()) }

            writeSettings()
        }

    private fun updateProjectsState(modify: Project.() -> Project) =
        onDefault {
            val projects = state.value.projects.toMutableList()
            projects.replaceAll {
                if (it.name == state.value.currentProject?.name)
                    it.modify().copy(lastModified = currentTime())
                else it
            }

            updateState {
                copy(
                    projects = projects.toList(),
                    currentProject = currentProject?.modify()
                )
            }

            writeSettings()
        }

    fun updatePlayerCount(playerNumber: Int) =
        updateProjectsState { copy(playerNumber = playerNumber) }

    fun updateTeamCount(teamNumber: Int) =
        updateProjectsState { copy(teamNumber = teamNumber) }

    fun setFields(newFields: Int) =
        updateProjectsState { copy(fields = newFields) }

    fun setTimeout(newTimeout: Int) =
        updateProjectsState { copy(timeout = newTimeout) }

    fun setGamePointsForBye(newGamePointsForBye: Int) =
        updateProjectsState { copy(gamePointsForBye = newGamePointsForBye) }

    fun setSetPointsForBye(newSetPointsForBye: Int) =
        updateProjectsState { copy(setPointsForBye = newSetPointsForBye) }

    fun setPointsForBye(newPointsForBye: Int) =
        updateProjectsState { copy(pointsForBye = newPointsForBye) }
}