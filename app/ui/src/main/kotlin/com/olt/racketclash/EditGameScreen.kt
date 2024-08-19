package com.olt.racketclash

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.data.Player
import com.olt.racketclash.editGame.EditGameModel
import com.olt.racketclash.language.Language
import com.olt.racketclash.navigate.Screens
import com.olt.racketclash.ui.*

@Composable
fun EditGameScreen(
    model: EditGameModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    TournamentScaffold(
        language = language,
        projectId = state.projectId,
        topAppBarTitle = language.editGame,
        hasBackPress = true,
        selectedTab = TournamentTabs.Games(language = language, projectId = state.projectId),
        navigateTo = { navigateTo(it) }
    ) {
        SettingsView {
            EditGameView(
                state = state,
                model = model,
                language = language,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
private fun EditGameView(
    state: EditGameModel.State,
    model: EditGameModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    Text(
        text = state.roundName,
        style = MaterialTheme.typography.headlineLarge
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically)  {
                BorderText<EditGameModel.SelectedPlayer.Left1>(
                    selectedPlayer = state.selectedPlayer,
                    text = state.player1LeftDisplayName
                )
                if (state.player1Left == null)
                    EditButton { model.selectPlayer(EditGameModel.SelectedPlayer.Left1) }
                else
                    DeleteButton { model.removePlayer(EditGameModel.SelectedPlayer.Left1) }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BorderText<EditGameModel.SelectedPlayer.Left2>(
                    selectedPlayer = state.selectedPlayer,
                    text = state.player2LeftDisplayName
                )
                if (state.player2Left == null)
                    EditButton { model.selectPlayer(EditGameModel.SelectedPlayer.Left2) }
                else
                    DeleteButton{ model.removePlayer(EditGameModel.SelectedPlayer.Left2) }
            }
        }

        Text(modifier = Modifier.padding(horizontal = 20.dp), text = language.vs)

        Column {
            Row(verticalAlignment = Alignment.CenterVertically)  {
                BorderText<EditGameModel.SelectedPlayer.Right1>(
                    selectedPlayer = state.selectedPlayer,
                    text = state.player1RightDisplayName
                )
                if (state.player1Right == null)
                    EditButton { model.selectPlayer(EditGameModel.SelectedPlayer.Right1) }
                else
                    DeleteButton { model.removePlayer(EditGameModel.SelectedPlayer.Right1) }
            }

            Row(verticalAlignment = Alignment.CenterVertically)  {
                BorderText<EditGameModel.SelectedPlayer.Right2>(
                    selectedPlayer = state.selectedPlayer,
                    text = state.player2RightDisplayName
                )
                if (state.player2Right == null)
                    EditButton { model.selectPlayer(EditGameModel.SelectedPlayer.Right2) }
                else
                    DeleteButton { model.removePlayer(EditGameModel.SelectedPlayer.Right2) }
            }
        }

        Checkbox(
            modifier = Modifier.padding(start = 20.dp),
            checked = state.isBye,
            onCheckedChange = model::setIsBye
        )
        Text(language.bye)
    }

    CancelSaveButtonRow(
        language = language,
        onCancel = { navigateTo(Screens.Pop) },
        onSave = { model.addGame() }
    )

    LazyTableWithScrollScaffold(
        modifier = Modifier.requiredHeightIn(max = 500.dp),
        topBarTitle = language.players,
        topBarActions = { Filter(state = state, model = model, language = language) },
        items = state.players,
        onClick = { model.setPlayer(it.id) },
        columns = listOf(
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
            )
        )
    )
}

@Composable
private inline fun <reified T: EditGameModel.SelectedPlayer> BorderText(
    selectedPlayer: EditGameModel.SelectedPlayer?,
    text: String
) {
    Text(
        modifier = if (selectedPlayer is T)
            Modifier.border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
        else Modifier,
        text = text
    )
}

@Composable
private fun Filter(
    state: EditGameModel.State,
    model: EditGameModel,
    language: Language
) {
    Checkbox(
        checked = state.filterNotInRound,
        onCheckedChange = model::changeFilterNotInRound
    )
    Text(language.hasNoGameInRound)

    TextField(
        modifier = Modifier.padding(horizontal = 5.dp).width(TextFieldDefaults.MinWidth),
        value = state.nameFilter,
        onValueChange = model::changeNameFilter,
        label = { Text(language.filterByName) },
        singleLine = true
    )

    DropDownMenu(
        modifier = Modifier.width(TextFieldDefaults.MinWidth),
        label = language.team,
        items = Player.sortingOptions(),
        value = state.sortedBy,
        textMapper = { it.text(language) },
        onClick = model::changeSorting
    )
}