package com.olt.racketclash.navigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.olt.racketclash.*
import com.olt.racketclash.data.database.Database
import com.olt.racketclash.editGame.EditGameModel
import com.olt.racketclash.editPlayer.EditPlayerModel
import com.olt.racketclash.editRound.EditRoundModel
import com.olt.racketclash.editTeam.EditTeamModel
import com.olt.racketclash.games.GamesModel
import com.olt.racketclash.language.LanguageModel
import com.olt.racketclash.newProject.NewProjectModel
import com.olt.racketclash.newRound.NewRoundModel
import com.olt.racketclash.players.PlayersModel
import com.olt.racketclash.projects.ProjectsModel
import com.olt.racketclash.teams.TeamsModel

@Composable
fun RacketClashNavigator(database: Database) {
    val model = viewModel { LanguageModel() }
    val state by model.state.collectAsState()

    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Projects"
    ) {
        composable(route = "Projects") {
            ProjectsScreen(
                model = viewModel { ProjectsModel(projectDatabase = database.projectDatabase) },
                language = state.language,
                availableLanguages = state.availableLanguages.toList(),
                navigateTo = { navigateTo(navController = navController, screen = it) },
                changeLanguage = model::setLanguage
            )
        }
        composable(route = "NewProject") {
            NewProjectScreen(
                model = viewModel { NewProjectModel(projectDatabase = database.projectDatabase) },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(route = "Teams/{projectId}") {
            TeamsScreen(
                model = viewModel {
                    TeamsModel(
                        projectDatabase = database.projectDatabase,
                        teamDatabase = database.teamDatabase,
                        projectId = it.getNullableLong("projectId")!!
                    )
                },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(
            route = "EditTeam/{teamId}/{projectId}",
            arguments = listOf(nullableLong(name = "teamId"))
        ) {
            EditTeamScreen(
                model = viewModel {
                    EditTeamModel(
                        teamDatabase = database.teamDatabase,
                        teamId = it.getNullableLong(key = "teamId"),
                        projectId = it.getNullableLong(key = "projectId")!!
                    )
                },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(route = "Players/{projectId}") {
            PlayersScreen(
                model = viewModel {
                    PlayersModel(
                        projectDatabase = database.projectDatabase,
                        playerDatabase = database.playerDatabase,
                        projectId = it.getNullableLong("projectId")!!)
                },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(
            route = "EditPlayer/{playerId}/{projectId}",
            arguments = listOf(nullableLong(name = "playerId"))
        ) {
            EditPlayerScreen(
                model = viewModel {
                    EditPlayerModel(
                        teamDatabase = database.teamDatabase,
                        playerDatabase = database.playerDatabase,
                        playerId = it.getNullableLong(key = "playerId"),
                        projectId = it.getNullableLong(key = "projectId")!!
                    )
                },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(route = "Games/{projectId}") {
            GamesScreen(
                model = viewModel {
                    GamesModel(
                        projectDatabase = database.projectDatabase,
                        roundDatabase = database.roundDatabase,
                        byeDatabase = database.byeDatabase,
                        gameDatabase = database.gameDatabase,
                        projectId = it.getNullableLong(key = "projectId")!!
                    )
                },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(route = "NewRound/{projectId}") {
            NewRoundScreen(
                model = viewModel {
                    NewRoundModel(
                        playerDatabase = database.playerDatabase,
                        roundDatabase = database.roundDatabase,
                        gameDatabase = database.gameDatabase,
                        projectId = it.getNullableLong(key = "projectId")!!
                    )
                },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(
            route = "EditGame/{roundId}/{projectId}",
            arguments = listOf(nullableLong(name = "roundId"))
        ) {
            EditGameScreen(
                model = viewModel {
                    EditGameModel(
                        projectDatabase = database.projectDatabase,
                        roundDatabase = database.roundDatabase,
                        byeDatabase = database.byeDatabase,
                        playerDatabase = database.playerDatabase,
                        gameDatabase = database.gameDatabase,
                        projectId = it.getNullableLong(key = "projectId")!!,
                        roundId = it.getNullableLong(key = "roundId")!!
                    )
                },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
        composable(
            route = "EditRound/{roundId}/{projectId}",
            arguments = listOf(nullableLong(name = "roundId"))
        ) {
            EditRoundScreen(
                model = viewModel {
                    EditRoundModel(
                        roundDatabase = database.roundDatabase,
                        byeDatabase = database.byeDatabase,
                        gameDatabase = database.gameDatabase,
                        roundId = it.getNullableLong(key = "roundId")!!,
                        projectId = it.getNullableLong(key = "projectId")!!
                    )
                },
                language = state.language,
                navigateTo = { navigateTo(navController = navController, screen = it) }
            )
        }
    }
}

private fun navigateTo(
    navController: NavController,
    screen: Screens
) {
    when (screen) {
        is Screens.EditGame -> navController.navigate(route = "${screen.name}/${screen.roundId}/${screen.projectId}")
        is Screens.EditPlayer -> navController.navigate(route = "${screen.name}/${screen.playerId}/${screen.projectId}")
        is Screens.EditRound -> navController.navigate(route = "${screen.name}/${screen.roundId}/${screen.projectId}")
        is Screens.EditTeam -> navController.navigate(route = "${screen.name}/${screen.teamId}/${screen.projectId}")
        is Screens.Games -> navController.navigate(route = "${screen.name}/${screen.projectId}") { popUpTo(route = Screens.Projects().name) }
        is Screens.NewProject -> navController.navigate(route = screen.name)
        is Screens.NewRound -> navController.navigate(route = "${screen.name}/${screen.projectId}")
        is Screens.Players -> navController.navigate(route = "${screen.name}/${screen.projectId}") { popUpTo(route = Screens.Projects().name) }
        Screens.Pop -> navController.popBackStack()
        is Screens.Projects -> navController.popBackStack(route = screen.name, inclusive = false)
        is Screens.Teams -> navController.navigate(route = "${screen.name}/${screen.projectId}") { popUpTo(route = Screens.Projects().name) }
    }
}

private fun nullableLong(name: String): NamedNavArgument =
    navArgument(name = name) { type = NavType.StringType; nullable = true }

private fun NavBackStackEntry.getNullableLong(key: String): Long? =
    arguments?.getString(key = key)?.toLongOrNull()
