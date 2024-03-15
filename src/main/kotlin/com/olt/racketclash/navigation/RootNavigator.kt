package com.olt.racketclash.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import com.olt.racketclash.screens.editPlayer.EditPlayerModel
import com.olt.racketclash.screens.editPlayer.EditPlayerScreen
import com.olt.racketclash.screens.editRound.EditRoundModel
import com.olt.racketclash.screens.editTeam.EditTeamModel
import com.olt.racketclash.screens.editTeam.EditTeamScreen
import com.olt.racketclash.screens.newProject.NewProjectModel
import com.olt.racketclash.screens.projects.ProjectsModel
import com.olt.racketclash.screens.newProject.NewProjectScreen
import com.olt.racketclash.screens.players.PlayersModel
import com.olt.racketclash.screens.players.PlayersScreen
import com.olt.racketclash.screens.projects.ProjectsScreen
import com.olt.racketclash.screens.rounds.RoundsModel
import com.olt.racketclash.screens.rounds.RoundsScreen
import com.olt.racketclash.screens.teams.TeamsModel
import com.olt.racketclash.screens.teams.TeamsScreen

class RootNavigator {
    private val fileHandler: FileHandler = FileHandler()
    private var database: Database? = null

    fun defaultScreen() : ProjectsScreen =
        ProjectsScreen(::projectsModelBuilder)

    private fun projectsModelBuilder(): ProjectsModel =
        ProjectsModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler)

    private fun newProjectModelBuilder(): NewProjectModel =
        NewProjectModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler)

    private fun teamsModelBuilder(): TeamsModel =
        TeamsModel(navigateToScreen = ::navigateTo, database = database!!)

    private fun editTeamModelBuilder(team: Team?): EditTeamModel =
        EditTeamModel(navigateToScreen = ::navigateTo, database = database!!, team = team)

    private fun playersModelBuilder(): PlayersModel =
        PlayersModel(navigateToScreen = ::navigateTo, database = database!!)

    private fun editPlayerModelBuilder(player: Player?): EditPlayerModel =
        EditPlayerModel(navigateToScreen = ::navigateTo, database = database!!, player = player)

    private fun roundsModelBuilder(): RoundsModel =
        RoundsModel(navigateToScreen = ::navigateTo, database = database!!)

    private fun editRoundModelBuilder(): EditRoundModel =
        EditRoundModel(navigateToScreen = ::navigateTo, database = database!!)

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
            Screens.Rounds -> navigateToRounds(navigator = navigator)
            Screens.EditRound -> {}
        }
    }

    private fun navigateToProjects(navigator: Navigator) {
        database = null
        navigator.replaceAll(ProjectsScreen(::projectsModelBuilder))
    }

    private fun navigateToNewProject(navigator: Navigator) {
        navigator.push(NewProjectScreen(::newProjectModelBuilder))
    }

    private fun openProject(navigator: Navigator, location: String, projectName: String) {
        database = Database(tournamentPath = location, fileHandler = fileHandler, projectName = projectName)
        navigateToTeams(navigator = navigator)
    }

    private fun navigateToTeams(navigator: Navigator) {
        navigator.replaceAll(TeamsScreen(::teamsModelBuilder))
    }

    private fun navigateToEditTeam(navigator: Navigator, team: Team?) {
        navigator.push(item = EditTeamScreen { editTeamModelBuilder(team = team) })
    }

    private fun navigateToPlayers(navigator: Navigator) {
        navigator.replaceAll(PlayersScreen(::playersModelBuilder))
    }

    private fun navigateToEditPlayer(navigator: Navigator, player: Player?) {
        navigator.push(item = EditPlayerScreen { editPlayerModelBuilder(player = player) })
    }

    private fun navigateToRounds(navigator: Navigator) {
        navigator.replaceAll(RoundsScreen(::roundsModelBuilder))
    }
}