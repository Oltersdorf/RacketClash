package com.olt.racketclash.app.screens.newProject

import com.olt.racketclash.data.database.Database
import com.olt.racketclash.language.LanguageModel
import com.olt.racketclash.state.ViewModelState

class NewProjectModel(
    private val database: Database
) : ViewModelState<NewProjectModel.State>(initialState = State()) {

    init {
        onIO {
            database.projects().collect {
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
            database.addProject(name = state.value.projectName)
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