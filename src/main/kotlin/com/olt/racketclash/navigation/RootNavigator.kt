package com.olt.racketclash.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.language.LanguageHandler
import com.olt.racketclash.language.translations.Language
import com.olt.racketclash.screens.editGame.EditGameModel
import com.olt.racketclash.screens.editGame.EditGameScreen
import com.olt.racketclash.screens.editPlayer.EditPlayerModel
import com.olt.racketclash.screens.editPlayer.EditPlayerScreen
import com.olt.racketclash.screens.editRound.EditRoundModel
import com.olt.racketclash.screens.editRound.EditRoundScreen
import com.olt.racketclash.screens.newRound.NewRoundModel
import com.olt.racketclash.screens.newRound.NewRoundScreen
import com.olt.racketclash.screens.editTeam.EditTeamModel
import com.olt.racketclash.screens.editTeam.EditTeamScreen
import com.olt.racketclash.screens.newProject.NewProjectModel
import com.olt.racketclash.screens.projects.ProjectsModel
import com.olt.racketclash.screens.newProject.NewProjectScreen
import com.olt.racketclash.screens.players.PlayersModel
import com.olt.racketclash.screens.players.PlayersScreen
import com.olt.racketclash.screens.projects.ProjectsScreen
import com.olt.racketclash.screens.games.GamesModel
import com.olt.racketclash.screens.games.GamesScreen
import com.olt.racketclash.screens.teams.TeamsModel
import com.olt.racketclash.screens.teams.TeamsScreen

class RootNavigator {
    private val fileHandler: FileHandler = FileHandler()
    private val languageHandler: LanguageHandler = LanguageHandler(fileHandler = fileHandler)
    private var database: Database? = null

    fun defaultScreen() : ProjectsScreen =
        ProjectsScreen(::projectsModelBuilder)

    private fun projectsModelBuilder(): ProjectsModel =
        ProjectsModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler, languageHandler = languageHandler)

    private fun newProjectModelBuilder(language: Language): NewProjectModel =
        NewProjectModel(navigateToScreen = ::navigateTo, fileHandler = fileHandler, language = language)

    private fun teamsModelBuilder(language: Language): TeamsModel =
        TeamsModel(navigateToScreen = ::navigateTo, database = database!!, language = language)

    private fun editTeamModelBuilder(team: Team?, language: Language): EditTeamModel =
        EditTeamModel(navigateToScreen = ::navigateTo, database = database!!, team = team, language = language)

    private fun playersModelBuilder(language: Language): PlayersModel =
        PlayersModel(navigateToScreen = ::navigateTo, database = database!!, language = language)

    private fun editPlayerModelBuilder(player: Player?, language: Language): EditPlayerModel =
        EditPlayerModel(navigateToScreen = ::navigateTo, database = database!!, player = player, language = language)

    private fun gamesModelBuilder(language: Language): GamesModel =
        GamesModel(navigateToScreen = ::navigateTo, database = database!!, fileHandler = fileHandler, language = language)

    private fun newRoundModelBuilder(language: Language): NewRoundModel =
        NewRoundModel(navigateToScreen = ::navigateTo, database = database!!, language = language)

    private fun editRoundModelBuilder(round: Round, language: Language): EditRoundModel =
        EditRoundModel(navigateToScreen = ::navigateTo, database = database!!, round = round, language = language)

    private fun editGameModelBuilder(roundId: Long, language: Language): EditGameModel =
        EditGameModel(navigateToScreen = ::navigateTo, database = database!!, roundId = roundId, language = language)

    private fun navigateTo(screens: Screens, navigator: Navigator) {
        when (screens) {
            Screens.Pop -> navigator.pop()
            Screens.Projects -> navigateToProjects(navigator = navigator)
            is Screens.NewProject -> navigateToNewProject(navigator = navigator, language = screens.language)
            is Screens.OpenProject -> openProject(navigator = navigator, project = screens.project, language = screens.language)
            is Screens.Teams -> navigateToTeams(navigator = navigator, language = screens.language)
            is Screens.EditTeam -> navigateToEditTeam(navigator = navigator, team = screens.team, language = screens.language)
            is Screens.Players -> navigateToPlayers(navigator = navigator, language = screens.language)
            is Screens.EditPlayer -> navigateToEditPlayer(navigator = navigator, player = screens.player, language = screens.language)
            is Screens.Games -> navigateToGames(navigator = navigator, language = screens.language)
            is Screens.NewRound -> navigateToNewRound(navigator = navigator, language = screens.language)
            is Screens.EditRound -> navigateToEditRound(navigator = navigator, round = screens.round, language = screens.language)
            is Screens.EditGame -> navigateToEditGame(navigator = navigator, roundId = screens.roundId, language = screens.language)
        }
    }

    private fun navigateToProjects(navigator: Navigator) {
        database = null
        navigator.replaceAll(ProjectsScreen(::projectsModelBuilder))
    }

    private fun navigateToNewProject(navigator: Navigator, language: Language) {
        navigator.push(NewProjectScreen { newProjectModelBuilder(language = language) })
    }

    private fun openProject(navigator: Navigator, project: Project, language: Language) {
        fileHandler.setCurrentProject(project = project)
        database = Database(tournamentPath = project.location, fileHandler = fileHandler)
        navigateToTeams(navigator = navigator, language = language)
    }

    private fun navigateToTeams(navigator: Navigator, language: Language) {
        navigator.replaceAll(TeamsScreen { teamsModelBuilder(language = language) })
    }

    private fun navigateToEditTeam(navigator: Navigator, team: Team?, language: Language) {
        navigator.push(item = EditTeamScreen { editTeamModelBuilder(team = team, language = language) })
    }

    private fun navigateToPlayers(navigator: Navigator, language: Language) {
        navigator.replaceAll(PlayersScreen { playersModelBuilder(language = language) })
    }

    private fun navigateToEditPlayer(navigator: Navigator, player: Player?, language: Language) {
        navigator.push(item = EditPlayerScreen { editPlayerModelBuilder(player = player, language = language) })
    }

    private fun navigateToGames(navigator: Navigator, language: Language) {
        navigator.replaceAll(GamesScreen { gamesModelBuilder(language = language) })
    }

    private fun navigateToNewRound(navigator: Navigator, language: Language) {
        navigator.push(item = NewRoundScreen { newRoundModelBuilder(language = language) })
    }

    private fun navigateToEditRound(navigator: Navigator, round: Round, language: Language) {
        navigator.push(item = EditRoundScreen { editRoundModelBuilder(round = round, language = language) })
    }

    private fun navigateToEditGame(navigator: Navigator, roundId: Long, language: Language) {
        navigator.push(item = EditGameScreen { editGameModelBuilder(roundId = roundId, language = language) })
    }
}