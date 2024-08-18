package com.olt.racketclash.app.screens.projects

import com.olt.racketclash.data.Project
import com.olt.racketclash.data.database.Database
import com.olt.racketclash.state.ViewModelState

class ProjectsModel(
    private val database: Database
): ViewModelState<ProjectsModel.State>(initialState = State()) {

    init {
        onIO {
            database.projects().collect {
                updateState {
                    copy(projects = it)
                }
            }
        }
    }

    data class State(
        val projects: List<Project> = emptyList()
    )

    fun deleteProject(id: Long) =
        onIO {
            database.deleteProject(id = id)
        }
}