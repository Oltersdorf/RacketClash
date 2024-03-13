package com.olt.racketclash.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.screens.projects.ProjectModel
import com.olt.racketclash.screens.newProject.NewProjectScreen
import com.olt.racketclash.screens.projects.ProjectsScreen
import com.olt.racketclash.screens.teams.TeamsScreen
import java.nio.file.Path

class RootNavigator {
    private var database: Database? = null

    fun defaultScreen() : ProjectsScreen =
        ProjectsScreen(ProjectModel(::navigateTo))

    private fun navigateTo(screens: Screens, navigator: Navigator) {
        when (screens) {
            Screens.Projects -> navigateToProjects(navigator = navigator)
            is Screens.NewProject -> navigateToNewProject(navigator = navigator, projectNames = screens.projectNames, addProject = screens.addProject)
            is Screens.OpenProject -> openProject(navigator = navigator, location = screens.projectLocation)
            is Screens.Teams -> navigateToTeams(navigator = navigator)
        }
    }

    private fun navigateToProjects(navigator: Navigator) {
        database = null
        navigator.replaceAll(ProjectsScreen(ProjectModel(::navigateTo)))
    }

    private fun navigateToNewProject(navigator: Navigator, projectNames: List<String>, addProject: (name: String, location: Path) -> Unit) {
        navigator.push(NewProjectScreen(projectNames = projectNames, addProject = addProject))
    }

    private fun openProject(navigator: Navigator, location: String) {
        database = Database(tournamentPath = location)
        navigateToTeams(navigator = navigator)
    }

    private fun navigateToTeams(navigator: Navigator) {
        navigator.replaceAll(TeamsScreen(database = database!!))
    }
}