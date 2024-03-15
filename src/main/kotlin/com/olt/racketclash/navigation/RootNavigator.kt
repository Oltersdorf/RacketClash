package com.olt.racketclash.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import com.olt.racketclash.screens.editPlayer.EditPlayerModel
import com.olt.racketclash.screens.editPlayer.EditPlayerScreen
import com.olt.racketclash.screens.editTeam.EditTeamModel
import com.olt.racketclash.screens.editTeam.EditTeamScreen
import com.olt.racketclash.screens.newProject.NewProjectModel
import com.olt.racketclash.screens.projects.ProjectsModel
import com.olt.racketclash.screens.newProject.NewProjectScreen
import com.olt.racketclash.screens.players.PlayersModel
import com.olt.racketclash.screens.players.PlayersScreen
import com.olt.racketclash.screens.projects.ProjectsScreen
import com.olt.racketclash.screens.teams.TeamsModel
import com.olt.racketclash.screens.teams.TeamsScreen

class RootNavigator {
    private val fileHandler: FileHandler = FileHandler()
    private var database: Database? = null

    fun defaultScreen() : ProjectsScreen =
        ProjectsScreen(ProjectsModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler))

    private fun navigateTo(screens: Screens, navigator: Navigator) {
        when (screens) {
            Screens.Pop -> navigator.pop()
            Screens.Projects -> navigateToProjects(navigator = navigator)
            Screens.NewProject -> navigateToNewProject(navigator = navigator)
            is Screens.OpenProject -> openProject(navigator = navigator, location = screens.projectLocation, projectName = screens.projectName)
            Screens.Teams -> navigateToTeams(navigator = navigator)
            is Screens.EditTeam -> navigateToEditTeam(navigator = navigator, team = screens.team)
            Screens.Players -> navigateToPlayers(navigator = navigator)
            is Screens.EditPlayer -> navigateToEditPlayer(navigator = navigator, player = screens.player)
            Screens.Games -> {}
        }
    }

    private fun navigateToProjects(navigator: Navigator) {
        database = null
        navigator.replaceAll(ProjectsScreen(ProjectsModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler)))
    }

    private fun navigateToNewProject(navigator: Navigator) {
        navigator.push(NewProjectScreen(NewProjectModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler)))
    }

    private fun openProject(navigator: Navigator, location: String, projectName: String) {
        database = Database(tournamentPath = location, fileHandler = fileHandler, projectName = projectName)
        navigateToTeams(navigator = navigator)
    }

    private fun navigateToTeams(navigator: Navigator) {
        navigator.replaceAll(TeamsScreen(TeamsModel(navigateToScreen = ::navigateTo, database = database!!)))
    }

    private fun navigateToEditTeam(navigator: Navigator, team: Team?) {
        navigator.push(item = EditTeamScreen(EditTeamModel(navigateToScreen = ::navigateTo, database = database!!, team = team)))
    }

    private fun navigateToPlayers(navigator: Navigator) {
        navigator.replaceAll(PlayersScreen(PlayersModel(navigateToScreen = ::navigateTo, database = database!!)))
    }

    private fun navigateToEditPlayer(navigator: Navigator, player: Player?) {
        navigator.push(item = EditPlayerScreen(EditPlayerModel(navigateToScreen = ::navigateTo, database = database!!, player = player)))
    }
}