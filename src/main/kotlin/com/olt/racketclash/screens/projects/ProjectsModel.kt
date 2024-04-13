package com.olt.racketclash.screens.projects

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.data.Project
import com.olt.racketclash.language.LanguageHandler
import com.olt.racketclash.language.translations.Language
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectsModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val fileHandler: FileHandler,
    private val languageHandler: LanguageHandler
) : NavigableStateScreenModel<ProjectsModel.Model>(navigateToScreen, Model(
    selectedLanguage = languageHandler.availableLanguages.find { it == fileHandler.selectedLanguage } ?: languageHandler.availableLanguages.first(),
    availableLanguages = languageHandler.availableLanguages.toList(),
    language = languageHandler.setLanguage(language = fileHandler.selectedLanguage)
)) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            fileHandler.projects().collect { projects ->
                updateState { copy(projects = projects) }
            }
        }
    }

    data class Model(
        val language: Language,
        val availableLanguages: List<String>,
        val selectedLanguage: String,
        val projects: List<Project> = emptyList()
    )

    fun deleteProject(name: String) {
        screenModelScope.launch(context = Dispatchers.IO) {
            fileHandler.deleteProject(name = name)
        }
    }

    fun changeLanguage(newLanguage: String) {
        screenModelScope.launch(context = Dispatchers.IO) {
            updateState { copy(language = languageHandler.setLanguage(language = newLanguage)) }
        }
    }
}