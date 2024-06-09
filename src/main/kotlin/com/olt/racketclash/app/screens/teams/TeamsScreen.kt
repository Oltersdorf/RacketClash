package com.olt.racketclash.app.screens.teams

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
import com.olt.racketclash.data.Team
import com.olt.racketclash.app.Screens
import com.olt.racketclash.language.Language
import com.olt.racketclash.ui.*

@Composable
fun TeamsScreen(
    model: TeamsModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    TournamentScaffold(
        language = language,
        topAppBarTitle = language.teams,
        topAppBarActions = {
            AddButton { navigateTo(Screens.EditTeam(teamId = null)) }
        },
        selectedTab = TournamentTabs.Teams(language = language),
        navigateTo = { navigateTo(it) }
    ) {
        TeamView(state = state, model = model, language = language, navigateTo = navigateTo)
    }
}

@Composable
private fun TeamView(
    language: Language,
    state: TeamsModel.State,
    model: TeamsModel,
    navigateTo: (Screens) -> Unit
) {
    if (state.isLoading)
        Loading()
    else
        TeamList(state = state, model = model, language = language, navigateTo = navigateTo)
}

@Composable
private fun TeamList(
    language: Language,
    state: TeamsModel.State,
    model: TeamsModel,
    navigateTo: (Screens) -> Unit
) {
    LazyTableWithScroll(
        header = {
            Spacer(modifier = Modifier.weight(1.0f))

            TextField(
                modifier = Modifier.width(TextFieldDefaults.MinWidth),
                value = state.filter,
                onValueChange = model::changeFilter,
                label = { Text(language.filterByName) },
                singleLine = true
            )

            DropDownMenu(
                modifier = Modifier.padding(start = 5.dp).width(TextFieldDefaults.MinWidth),
                label = language.sortBy,
                items = Team.sortingOptions(),
                value = state.sortedBy,
                textMapper = { it.text(language = language) },
                onClick = model::changeSorting
            )
        },
        items = state.teams,
        modifier = Modifier.padding(5.dp),
        onClick = { navigateTo(Screens.EditTeam(teamId = it.id)) },
        columns = listOf(
            LazyTableColumn.Text(
                name = language.name,
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = language.strength,
                weight = 1.0f,
                text = { it.strength.toString() }
            ),
            LazyTableColumn.Text(
                name = language.players,
                weight = 1.0f,
                text = { it.size.toString() }
            ),
            LazyTableColumn.Text(
                name = language.pending,
                weight = 1.0f,
                text = { it.openGames.toString() }
            ),
            LazyTableColumn.Text(
                name = language.played,
                weight = 1.0f,
                text = { it.played.toString() }
            ),
            LazyTableColumn.Text(
                name = language.byes,
                weight = 1.0f,
                text = { it.bye.toString() }
            ),
            LazyTableColumn.Text(
                name = language.games,
                weight = 1.0f,
                text = { "${it.wonGames} : ${it.lostGames}" }
            ),
            LazyTableColumn.Text(
                name = language.sets,
                weight = 1.0f,
                text = { "${it.wonSets} : ${it.lostSets}" }
            ),
            LazyTableColumn.Text(
                name = language.points,
                weight = 1.0f,
                text = { "${it.wonPoints} : ${it.lostPoints}" }
            ),
            LazyTableColumn.IconButton(
                name = language.delete,
                weight = 0.5f,
                headerTextAlign = TextAlign.Center,
                onClick = { model.deleteTeam(id = it.id) },
                enabled = { it.size == 0 },
                imageVector = Icons.Default.Delete,
                contentDescription = language.delete
            )
        )
    )
}