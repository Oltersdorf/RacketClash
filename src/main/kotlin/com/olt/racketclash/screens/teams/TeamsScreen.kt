package com.olt.racketclash.screens.teams

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
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
    LazyColumnWithScroll(
        modifier = Modifier.padding(paddingValues = paddingValues).padding(5.dp),
        header = {
            Surface(
                tonalElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Name",
                        modifier = Modifier.weight(5.0f)
                    )
                    Text(
                        text = "Difficulty",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "Player",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "Edit",
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Delete",
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    ) {
        items(items = teams) {
            val navigator = LocalNavigator.currentOrThrow

            Row(
                modifier = Modifier.clickable {},
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.name,
                    modifier = Modifier.weight(5.0f)
                )
                Text(
                    text = "${it.strength}",
                    modifier = Modifier.weight(1.0f)
                )
                Text(
                    text = "${it.size}",
                    modifier = Modifier.weight(1.0f)
                )
                IconButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = { navigateTo(Screens.EditTeam(team = it), navigator) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
                IconButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = { deleteTeam(it.id) },
                    enabled = it.size == 0
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Edit"
                    )
                }
            }
        }
    }
}