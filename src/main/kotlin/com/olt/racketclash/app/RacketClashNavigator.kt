package com.olt.racketclash.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.olt.racketclash.app.screens.editGame.EditGameModel
import com.olt.racketclash.app.screens.editGame.EditGameScreen
import com.olt.racketclash.app.screens.editPlayer.EditPlayerModel
import com.olt.racketclash.app.screens.editPlayer.EditPlayerScreen
import com.olt.racketclash.app.screens.editRound.EditRoundModel
import com.olt.racketclash.app.screens.editRound.EditRoundScreen
import com.olt.racketclash.app.screens.editTeam.EditTeamModel
import com.olt.racketclash.app.screens.editTeam.EditTeamScreen
import com.olt.racketclash.app.screens.games.GamesModel
import com.olt.racketclash.app.screens.games.GamesScreen
import com.olt.racketclash.app.screens.newProject.NewProjectModel
import com.olt.racketclash.app.screens.newProject.NewProjectScreen
import com.olt.racketclash.app.screens.newRound.NewRoundModel
import com.olt.racketclash.app.screens.newRound.NewRoundScreen
import com.olt.racketclash.app.screens.players.PlayersModel
import com.olt.racketclash.app.screens.players.PlayersScreen
import com.olt.racketclash.data.Project
import com.olt.racketclash.app.screens.projects.ProjectsScreen
import com.olt.racketclash.app.screens.teams.TeamsModel
import com.olt.racketclash.app.screens.teams.TeamsScreen

@Composable
fun RacketClashNavigator() {
    val model = viewModel { RacketClashModel() }
    val state by model.state.collectAsState()

    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Projects"
    ) {
        composable(route = "Projects") {
            ProjectsScreen(
                language = state.language,
                availableLanguages = state.availableLanguages.toList(),
                projects = state.projects,
                navigateTo = { navigateTo(navController = navController, screen = it, setCurrentProject = model::setCurrentProject) },
                changeLanguage = model::setLanguage,
                deleteProject = model::deleteProject
            )
        }
        composable(route = "NewProject") {
            NewProjectScreen(
                model = viewModel { NewProjectModel(addProject = model::addProject, projectNames = state.projects.map { it.name }) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(route = "Teams") {
            TeamsScreen(
                model = viewModel { TeamsModel(database = state.database!!, project = state.currentProject!!) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(
            route = "EditTeam/{teamId}",
            arguments = listOf(nullableLong(name = "teamId"))
        ) {
            EditTeamScreen(
                model = viewModel { EditTeamModel(database = state.database!!, teamId = it.getNullableLong(key = "teamId")) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(route = "Players") {
            PlayersScreen(
                model = viewModel { PlayersModel(database = state.database!!, project = state.currentProject) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(
            route = "EditPlayer/{playerId}",
            arguments = listOf(nullableLong(name = "playerId"))
        ) {
            EditPlayerScreen(
                model = viewModel { EditPlayerModel(database = state.database!!, playerId = it.getNullableLong(key = "playerId")) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(route = "Games") {
            GamesScreen(
                model = viewModel { GamesModel(database = state.database!!, appModel = model) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(route = "NewRound") {
            NewRoundScreen(
                model = viewModel { NewRoundModel(database = state.database!!) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(
            route = "EditGame/{roundId}",
            arguments = listOf(nullableLong(name = "roundId"))
        ) {
            EditGameScreen(
                model = viewModel { EditGameModel(database = state.database!!, project = state.currentProject, roundId = it.getNullableLong(key = "roundId")!!) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(
            route = "EditRound/{roundId}",
            arguments = listOf(nullableLong(name = "roundId"))
        ) {
            EditRoundScreen(
                model = viewModel { EditRoundModel(database = state.database!!, roundId = it.getNullableLong(key = "roundId")!!) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
    }
}

private fun navigateTo(
    navController: NavController,
    screen: Screens,
    setCurrentProject: (Project?) -> Unit = {}
) {
    when (screen) {
        is Screens.EditGame -> navController.navigate(route = "${screen.name}/${screen.roundId}")
        is Screens.EditPlayer -> navController.navigate(route = "${screen.name}/${screen.playerId}")
        is Screens.EditRound -> navController.navigate(route = "${screen.name}/${screen.roundId}")
        is Screens.EditTeam -> navController.navigate(route = "${screen.name}/${screen.teamId}")
        is Screens.Games -> navController.navigate(route = screen.name) { popUpTo(route = Screens.Projects().name) }
        is Screens.NewProject -> navController.navigate(route = screen.name)
        is Screens.NewRound -> navController.navigate(route = screen.name)
        is Screens.OpenProject -> {
            setCurrentProject(screen.project)
            navController.navigate(route = Screens.Teams().name)
        }
        is Screens.Players -> navController.navigate(route = screen.name) { popUpTo(route = Screens.Projects().name) }
        Screens.Pop -> navController.popBackStack()
        is Screens.Projects -> {
            setCurrentProject(null)
            navController.popBackStack(route = screen.name, inclusive = false)
        }
        is Screens.Teams -> navController.navigate(route = screen.name) { popUpTo(route = Screens.Projects().name) }
    }
}

private fun nullableLong(name: String): NamedNavArgument =
    navArgument(name = name) { type = NavType.StringType; nullable = true }

private fun NavBackStackEntry.getNullableLong(key: String): Long? =
    arguments?.getString(key = key)?.toLongOrNull()

