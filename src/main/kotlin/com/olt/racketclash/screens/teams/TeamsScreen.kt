package com.olt.racketclash.screens.teams

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

                AddButton { screenModel.navigateTo(screen = Screens.EditTeam(team = null), navigator = navigator) }
            },
            selectedTab = TournamentTabs.Teams,
            navigateTo = screenModel::navigateTo
        ) {
            TeamView(modal = stateModel, deleteTeam = screenModel::deleteTeam, navigateTo = screenModel::navigateTo)
        }
    }
}

@Composable
private fun TeamView(
    modal: TeamsModel.Modal,
    deleteTeam: (id: Long) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    if (modal.isLoading)
        Loading()
    else
        TeamList(teams = modal.teams, deleteTeam = deleteTeam, navigateTo = navigateTo)
}

@Composable
private fun TeamList(
    teams: List<Team>,
    deleteTeam: (id: Long) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    LazyTableWithScroll(
        items = teams,
        modifier = Modifier.padding(5.dp),
        onClick = { navigateTo(Screens.EditTeam(it), navigator) },
        columns = listOf(
            LazyTableColumn.Text(
                name = "Name",
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = "Difficulty",
                weight = 1.0f,
                text = { it.strength.toString() }
            ),
            LazyTableColumn.Text(
                name = "Player",
                weight = 1.0f,
                text = { it.size.toString() }
            ),
            LazyTableColumn.IconButton(
                name = "Delete",
                weight = 0.5f,
                headerTextAlign = TextAlign.Center,
                onClick = { deleteTeam(it.id) },
                enabled = { it.size == 0 },
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        )
    )
}