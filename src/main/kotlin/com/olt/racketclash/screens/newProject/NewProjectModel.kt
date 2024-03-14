package com.olt.racketclash.screens.newProject

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewProjectModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val fileHandler: FileHandler
) : NavigableStateScreenModel<NewProjectModel.Model>(navigateToScreen, Model()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            fileHandler.projects().collect { projects ->
                mutableState.value = Model(projectNames = projects.map { it.name })
                changeProjectName(newName = mutableState.value.projectName)
            }
        }
    }

    data class Model(
        val canCreate: Boolean = false,
        val projectName: String = "",
        val location: String = FileHandler.defaultProjectLocation,
        val projectNames: List<String> = emptyList()
    )

    fun addProject() {
        screenModelScope.launch(context = Dispatchers.IO) {
            fileHandler.addProject(name = mutableState.value.projectName, location = mutableState.value.location)
        }
    }

    fun changeProjectName(newName: String) {
        screenModelScope.launch(context = Dispatchers.Default) {
            if (mutableState.value.projectNames.contains(newName) || newName.isBlank())
                mutableState.value = mutableState.value.copy(canCreate = false, projectName = newName)
            else
                mutableState.value = mutableState.value.copy(canCreate = true, projectName = newName)
        }
    }

    fun changeLocation(newLocation: String?) {
        screenModelScope.launch(context = Dispatchers.Default) {
            if (newLocation != null)
                mutableState.value = mutableState.value.copy(location = newLocation)
        }
    }
}