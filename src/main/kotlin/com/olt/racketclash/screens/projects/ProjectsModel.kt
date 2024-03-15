package com.olt.racketclash.screens.projects

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.data.Project
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectsModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val fileHandler: FileHandler
) : NavigableStateScreenModel<ProjectsModel.Model>(navigateToScreen, Model()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            fileHandler.projects().collect { projects ->
                updateState { Model(projects = projects) }
            }
        }
    }

    data class Model(
        val projects: List<Project> = emptyList()
    )

    fun deleteProject(name: String) {
        screenModelScope.launch(context = Dispatchers.IO) {
            fileHandler.deleteProject(name = name)
        }
    }
}