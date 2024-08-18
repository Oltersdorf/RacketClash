package com.olt.racketclash.app.screens.projects

import com.olt.racketclash.data.Project
import com.olt.racketclash.data.database.IProjectDatabase
import com.olt.racketclash.state.ViewModelState

class ProjectsModel(
    private val projectDatabase: IProjectDatabase
): ViewModelState<ProjectsModel.State>(initialState = State()) {

    init {
        onIO {
            projectDatabase.projects().collect {
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
            projectDatabase.deleteProject(id = id)
        }
}