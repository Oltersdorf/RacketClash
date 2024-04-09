package com.olt.racketclash.screens.teams

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
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
            TeamView(model = stateModel, screenModel = screenModel)
        }
    }
}

@Composable
private fun TeamView(
    model: TeamsModel.Modal,
    screenModel: TeamsModel
) {
    if (model.isLoading)
        Loading()
    else
        TeamList(model = model, screenModel = screenModel)
}

@Composable
private fun TeamList(
    model: TeamsModel.Modal,
    screenModel: TeamsModel
) {
    val navigator = LocalNavigator.currentOrThrow

    LazyTableWithScroll(
        header = {
            Spacer(modifier = Modifier.weight(1.0f))

            TextField(
                modifier = Modifier.width(TextFieldDefaults.MinWidth),
                value = model.filter,
                onValueChange = screenModel::changeFilter,
                label = { Text("Filter by name") },
                singleLine = true
            )

            DropDownMenu(
                modifier = Modifier.padding(start = 5.dp).width(TextFieldDefaults.MinWidth),
                label = "Sort by",
                items = Team.sortingOptions(),
                value = model.sortedBy,
                textMapper = Team.Sorting::text,
                onClick = screenModel::changeSorting
            )
        },
        items = model.teams,
        modifier = Modifier.padding(5.dp),
        onClick = { screenModel.navigateTo(screen = Screens.EditTeam(it), navigator = navigator) },
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
            LazyTableColumn.Text(
                name = "pending",
                weight = 1.0f,
                text = { it.openGames.toString() }
            ),
            LazyTableColumn.Text(
                name = "played",
                weight = 1.0f,
                text = { it.played.toString() }
            ),
            LazyTableColumn.Text(
                name = "bye",
                weight = 1.0f,
                text = { it.bye.toString() }
            ),
            LazyTableColumn.Text(
                name = "Games",
                weight = 1.0f,
                text = { "${it.wonGames} : ${it.lostGames}" }
            ),
            LazyTableColumn.Text(
                name = "Sets",
                weight = 1.0f,
                text = { "${it.wonSets} : ${it.lostSets}" }
            ),
            LazyTableColumn.Text(
                name = "Points",
                weight = 1.0f,
                text = { "${it.wonPoints} : ${it.lostPoints}" }
            ),
            LazyTableColumn.IconButton(
                name = "Delete",
                weight = 0.5f,
                headerTextAlign = TextAlign.Center,
                onClick = { screenModel.deleteTeam(id = it.id) },
                enabled = { it.size == 0 },
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        )
    )
}