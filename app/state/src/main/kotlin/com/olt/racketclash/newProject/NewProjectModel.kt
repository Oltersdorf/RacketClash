package com.olt.racketclash.newProject

import com.olt.racketclash.data.database.IProjectDatabase
import com.olt.racketclash.state.ViewModelState

class NewProjectModel(
    private val projectDatabase: IProjectDatabase
) : ViewModelState<NewProjectModel.State>(initialState = State()) {

    init {
        onIO {
            projectDatabase.projects().collect {
                updateState { copy(projectNames = it.map { it.name }) }
            }
        }
    }

    data class State(
        val canCreate: Boolean = false,
        val projectName: String = "",
        val projectNames: List<String> = emptyList()
    )

    fun addProject() =
        onIO {
            projectDatabase.addProject(name = state.value.projectName)
        }

    fun changeProjectName(newName: String) {
        onDefault {
            if (state.value.projectNames.contains(newName) || newName.isBlank())
                updateState { copy(canCreate = false, projectName = newName) }
            else
                updateState { copy(canCreate = true, projectName = newName) }
        }
    }
}