package com.olt.racketclash.screens.editGame

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Player
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class EditGameScreen(private val modelBuilder: () -> EditGameModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            language = stateModel.language,
            topAppBarTitle = stateModel.language.editGame,
            hasBackPress = true,
            selectedTab = TournamentTabs.Games(language = stateModel.language),
            navigateTo = screenModel::navigateTo
        ) {
            SettingsView {
                EditGameView(
                    model = stateModel,
                    screenModel = screenModel
                )
            }
        }
    }
}

@Composable
private fun EditGameView(
    model: EditGameModel.Model,
    screenModel: EditGameModel
) {
    Text(
        text = model.roundName,
        style = MaterialTheme.typography.headlineLarge
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically)  {
                BorderText<EditGameModel.SelectedPlayer.Left1>(
                    selectedPlayer = model.selectedPlayer,
                    text = model.player1LeftDisplayName
                )
                if (model.player1Left == null)
                    EditButton { screenModel.selectPlayer(EditGameModel.SelectedPlayer.Left1) }
                else
                    DeleteButton { screenModel.removePlayer(EditGameModel.SelectedPlayer.Left1) }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BorderText<EditGameModel.SelectedPlayer.Left2>(
                    selectedPlayer = model.selectedPlayer,
                    text = model.player2LeftDisplayName
                )
                if (model.player2Left == null)
                    EditButton { screenModel.selectPlayer(EditGameModel.SelectedPlayer.Left2) }
                else
                    DeleteButton{ screenModel.removePlayer(EditGameModel.SelectedPlayer.Left2) }
            }
        }

        Text(modifier = Modifier.padding(horizontal = 20.dp), text = model.language.vs)

        Column {
            Row(verticalAlignment = Alignment.CenterVertically)  {
                BorderText<EditGameModel.SelectedPlayer.Right1>(
                    selectedPlayer = model.selectedPlayer,
                    text = model.player1RightDisplayName
                )
                if (model.player1Right == null)
                    EditButton { screenModel.selectPlayer(EditGameModel.SelectedPlayer.Right1) }
                else
                    DeleteButton { screenModel.removePlayer(EditGameModel.SelectedPlayer.Right1) }
            }

            Row(verticalAlignment = Alignment.CenterVertically)  {
                BorderText<EditGameModel.SelectedPlayer.Right2>(
                    selectedPlayer = model.selectedPlayer,
                    text = model.player2RightDisplayName
                )
                if (model.player2Right == null)
                    EditButton { screenModel.selectPlayer(EditGameModel.SelectedPlayer.Right2) }
                else
                    DeleteButton { screenModel.removePlayer(EditGameModel.SelectedPlayer.Right2) }
            }
        }
    }

    val navigator = LocalNavigator.currentOrThrow
    CancelSaveButtonRow(
        language = model.language,
        onCancel = { screenModel.navigateTo(Screens.Pop, navigator) },
        onSave = { screenModel.addGame() }
    )

    LazyTableWithScrollScaffold(
        modifier = Modifier.requiredHeightIn(max = 500.dp),
        topBarTitle = model.language.players,
        topBarActions = { Filter(model = model, screenModel = screenModel) },
        items = model.players,
        onClick = { screenModel.setPlayer(it.id) },
        columns = listOf(
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
    model: EditGameModel.Model,
    screenModel: EditGameModel
) {
    Checkbox(
        checked = model.filterNotInRound,
        onCheckedChange = screenModel::changeFilterNotInRound
    )
    Text(model.language.hasNoGameInRound)

    TextField(
        modifier = Modifier.padding(horizontal = 5.dp).width(TextFieldDefaults.MinWidth),
        value = model.nameFilter,
        onValueChange = screenModel::changeNameFilter,
        label = { Text(model.language.filterByName) },
        singleLine = true
    )

    DropDownMenu(
        modifier = Modifier.width(TextFieldDefaults.MinWidth),
        label = model.language.team,
        items = Player.sortingOptions(),
        value = model.sortedBy,
        textMapper = { it.text(model.language) },
        onClick = screenModel::changeSorting
    )
}