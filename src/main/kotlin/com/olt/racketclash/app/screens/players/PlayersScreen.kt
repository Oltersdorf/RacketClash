package com.olt.racketclash.app.screens.players

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
import com.olt.racketclash.data.Player
import com.olt.racketclash.app.Screens
import com.olt.racketclash.language.Language
import com.olt.racketclash.ui.*

@Composable
fun PlayersScreen(
    model: PlayersModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    TournamentScaffold(
        language = language,
        projectId = state.projectId,
        topAppBarTitle = language.players,
        topAppBarActions = {
            AddButton { navigateTo(Screens.EditPlayer(playerId = null, projectId = state.projectId)) }
        },
        selectedTab = TournamentTabs.Players(language = language, projectId = state.projectId),
        navigateTo = { navigateTo(it) }
    ) {
        PlayerView(
            state = state,
            model = model,
            language = language,
            navigateTo = navigateTo
        )
    }
}

@Composable
private fun PlayerView(
    state: PlayersModel.State,
    model: PlayersModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    if (state.isLoading)
        Loading()
    else
        PlayerList(
            state = state,
            model = model,
            language = language,
            navigateTo = navigateTo
        )
}

@Composable
private fun PlayerList(
    state: PlayersModel.State,
    model: PlayersModel,
    language: Language,
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
                items = Player.sortingOptions(),
                value = state.sortedBy,
                textMapper = { it.text(language = language) },
                onClick = model::changeSorting
            )
        },
        items = state.players,
        modifier = Modifier.padding(5.dp),
        onClick = { navigateTo(Screens.EditPlayer(playerId = it.id, projectId = state.projectId)) },
        columns = listOf(
            LazyTableColumn.Checkbox(
                name = language.active,
                weight = 1.0f,
                checked = { it.active },
                onCheckChanged = { item, checked -> model.updateActive(item.id, checked) }
            ),
            LazyTableColumn.Text(
                name = language.name,
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = language.team,
                weight = 2.0f,
                text = { it.teamName }
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
                onClick = { model.deletePlayer(it.id) },
                enabled = { it.openGames == 0 && it.bye == 0 && it.played == 0 },
                imageVector = Icons.Default.Delete,
                contentDescription = language.delete
            )
        )
    )
}