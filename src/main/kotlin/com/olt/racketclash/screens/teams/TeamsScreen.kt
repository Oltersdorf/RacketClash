package com.olt.racketclash.screens.teams

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Team
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class TeamsScreen(private val modelBuilder: () -> TeamsModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Teams",
            topAppBarActions = {
                val navigator = LocalNavigator.currentOrThrow

                IconButton(onClick = { screenModel.navigateTo(screen = Screens.EditTeam(team = null), navigator = navigator) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
            selectedTab = TournamentTabs.Teams,
            navigateTo = screenModel::navigateTo
        ) {
            TeamView(paddingValues = it, modal = stateModel, deleteTeam = screenModel::deleteTeam, navigateTo = screenModel::navigateTo)
        }
    }
}

@Composable
private fun TeamView(
    paddingValues: PaddingValues,
    modal: TeamsModel.Modal,
    deleteTeam: (id: Long) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    if (modal.isLoading)
        Loading(paddingValues = paddingValues)
    else
        TeamList(paddingValues = paddingValues, teams = modal.teams, deleteTeam = deleteTeam, navigateTo = navigateTo)
}

@Composable
private fun TeamList(
    paddingValues: PaddingValues,
    teams: List<Team>,
    deleteTeam: (id: Long) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    LazyTableWithScroll(
        items = teams,
        modifier = Modifier.padding(paddingValues = paddingValues).padding(5.dp),
        onClick = { navigateTo(Screens.EditTeam(it), navigator) },
        columns = listOf(
            LazyTableColumn(
                name = "Name",
                weight = 5.0f
            ) { item, weight ->
                Text(
                    text = item.name,
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Difficulty",
                weight = 1.0f
            ) {item, weight ->
                Text(
                    text = "${item.strength}",
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Player",
                weight = 1.0f
            ) {item, weight ->
                Text(
                    text = "${item.size}",
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Edit",
                weight = 0.5f,
                textAlign = TextAlign.Center
            ) { item, weight ->
                IconButton(
                    modifier = Modifier.weight(weight),
                    onClick = { navigateTo(Screens.EditTeam(team = item), navigator) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            },
            LazyTableColumn(
                name = "Delete",
                weight = 0.5f,
                textAlign = TextAlign.Center
            ) { item, weight ->
                IconButton(
                    modifier = Modifier.weight(weight),
                    onClick = { deleteTeam(item.id) },
                    enabled = item.size == 0
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Edit"
                    )
                }
            }
        )
    )
}