package com.olt.racketclash.screens.players

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
import com.olt.racketclash.data.Player
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class PlayersScreen(private val modelBuilder: () -> PlayersModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            language = stateModel.language,
            topAppBarTitle = stateModel.language.players,
            topAppBarActions = {
                val navigator = LocalNavigator.currentOrThrow
                AddButton { screenModel.navigateTo(Screens.EditPlayer(player = null, language = stateModel.language), navigator) }
            },
            selectedTab = TournamentTabs.Players(stateModel.language),
            navigateTo = screenModel::navigateTo
        ) {
            PlayerView(
                model = stateModel,
                screenModel = screenModel
            )
        }
    }
}

@Composable
private fun PlayerView(
    model: PlayersModel.Modal,
    screenModel: PlayersModel
) {
    if (model.isLoading)
        Loading()
    else
        PlayerList(
            model = model,
            screenModel = screenModel
        )
}

@Composable
private fun PlayerList(
    model: PlayersModel.Modal,
    screenModel: PlayersModel
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
                items = Player.sortingOptions(),
                value = model.sortedBy,
                textMapper = { it.text(language = model.language) },
                onClick = screenModel::changeSorting
            )
        },
        items = model.players,
        modifier = Modifier.padding(5.dp),
        onClick = { screenModel.navigateTo(Screens.EditPlayer(player = it, language = model.language), navigator) },
        columns = listOf(
            LazyTableColumn.Checkbox(
                name = model.language.active,
                weight = 1.0f,
                checked = { it.active },
                onCheckChanged = { item, checked -> screenModel.updateActive(item.id, checked) }
            ),
            LazyTableColumn.Text(
                name = model.language.name,
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = model.language.team,
                weight = 2.0f,
                text = { it.teamName }
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
                onClick = { screenModel.deletePlayer(it.id) },
                enabled = { it.openGames == 0 && it.bye == 0 && it.played == 0 },
                imageVector = Icons.Default.Delete,
                contentDescription = model.language.delete
            )
        )
    )
}