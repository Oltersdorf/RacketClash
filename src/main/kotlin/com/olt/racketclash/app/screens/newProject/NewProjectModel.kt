package com.olt.racketclash.app.screens.newProject

import com.olt.racketclash.app.RacketClashModel
import com.olt.racketclash.state.ViewModelState

class NewProjectModel(
    private val addProject: (name: String, location: String) -> Unit,
    projectNames: List<String>
) : ViewModelState<NewProjectModel.State>(initialState = State(projectNames = projectNames)) {

    data class State(
        val canCreate: Boolean = false,
        val projectName: String = "",
        val location: String = RacketClashModel.defaultProjectLocation,
        val projectNames: List<String>
    )

    fun addProject() =
        onIO {
            addProject(state.value.projectName, state.value.location)
        }

    fun changeProjectName(newName: String) {
        onDefault {
            if (state.value.projectNames.contains(newName) || newName.isBlank())
                updateState { copy(canCreate = false, projectName = newName) }
            else
                updateState { copy(canCreate = true, projectName = newName) }
        }
    }

    fun changeLocation(newLocation: String?) =
        onDefault {
            if (newLocation != null)
                updateState { copy(location = newLocation) }
        }
}