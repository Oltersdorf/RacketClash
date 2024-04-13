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
            language = stateModel.language,
            topAppBarTitle = stateModel.language.teams,
            topAppBarActions = {
                val navigator = LocalNavigator.currentOrThrow

                AddButton {
                    screenModel.navigateTo(
                        screen = Screens.EditTeam(team = null, language = stateModel.language),
                        navigator = navigator)
                }
            },
            selectedTab = TournamentTabs.Teams(language = stateModel.language),
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
                label = { Text(model.language.filterByName) },
                singleLine = true
            )

            DropDownMenu(
                modifier = Modifier.padding(start = 5.dp).width(TextFieldDefaults.MinWidth),
                label = model.language.sortBy,
                items = Team.sortingOptions(),
                value = model.sortedBy,
                textMapper = { it.text(language = model.language) },
                onClick = screenModel::changeSorting
            )
        },
        items = model.teams,
        modifier = Modifier.padding(5.dp),
        onClick = { screenModel.navigateTo(screen = Screens.EditTeam(team = it, language = model.language), navigator = navigator) },
        columns = listOf(
            LazyTableColumn.Text(
                name = model.language.name,
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = model.language.strength,
                weight = 1.0f,
                text = { it.strength.toString() }
            ),
            LazyTableColumn.Text(
                name = model.language.players,
                weight = 1.0f,
                text = { it.size.toString() }
            ),
            LazyTableColumn.Text(
                name = model.language.pending,
                weight = 1.0f,
                text = { it.openGames.toString() }
            ),
            LazyTableColumn.Text(
                name = model.language.played,
                weight = 1.0f,
                text = { it.played.toString() }
            ),
            LazyTableColumn.Text(
                name = model.language.byes,
                weight = 1.0f,
                text = { it.bye.toString() }
            ),
            LazyTableColumn.Text(
                name = model.language.games,
                weight = 1.0f,
                text = { "${it.wonGames} : ${it.lostGames}" }
            ),
            LazyTableColumn.Text(
                name = model.language.sets,
                weight = 1.0f,
                text = { "${it.wonSets} : ${it.lostSets}" }
            ),
            LazyTableColumn.Text(
                name = model.language.points,
                weight = 1.0f,
                text = { "${it.wonPoints} : ${it.lostPoints}" }
            ),
            LazyTableColumn.IconButton(
                name = model.language.delete,
                weight = 0.5f,
                headerTextAlign = TextAlign.Center,
                onClick = { screenModel.deleteTeam(id = it.id) },
                enabled = { it.size == 0 },
                imageVector = Icons.Default.Delete,
                contentDescription = model.language.delete
            )
        )
    )
}