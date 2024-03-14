package com.olt.racketclash.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.screens.newProject.NewProjectModel
import com.olt.racketclash.screens.projects.ProjectsModel
import com.olt.racketclash.screens.newProject.NewProjectScreen
import com.olt.racketclash.screens.projects.ProjectsScreen
import com.olt.racketclash.screens.teams.TeamsScreen

class RootNavigator {
    private val fileHandler: FileHandler = FileHandler()
    private var database: Database? = null

    fun defaultScreen() : ProjectsScreen =
        ProjectsScreen(ProjectsModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler))

    private fun navigateTo(screens: Screens, navigator: Navigator) {
        when (screens) {
            Screens.Projects -> navigateToProjects(navigator = navigator)
            Screens.NewProject -> navigateToNewProject(navigator = navigator)
            is Screens.OpenProject -> openProject(navigator = navigator, location = screens.projectLocation)
            is Screens.Teams -> navigateToTeams(navigator = navigator)
        }
    }

    private fun navigateToProjects(navigator: Navigator) {
        database = null
        navigator.replaceAll(ProjectsScreen(ProjectsModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler)))
    }

    private fun navigateToNewProject(navigator: Navigator) {
        navigator.push(NewProjectScreen(NewProjectModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler)))
    }

    private fun openProject(navigator: Navigator, location: String) {
        database = Database(tournamentPath = location)
        navigateToTeams(navigator = navigator)
    }

    private fun navigateToTeams(navigator: Navigator) {
        navigator.replaceAll(TeamsScreen(database = database!!))
    }
}