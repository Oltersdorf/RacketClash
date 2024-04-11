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
            topAppBarTitle = "Edit Game",
            hasBackPress = true,
            selectedTab = TournamentTabs.Games,
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

        Text(modifier = Modifier.padding(horizontal = 20.dp), text = "vs")

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
        onCancel = { screenModel.navigateTo(Screens.Pop, navigator) },
        onSave = { screenModel.addGame() }
    )

    LazyTableWithScrollScaffold(
        modifier = Modifier.requiredHeightIn(max = 500.dp),
        topBarTitle = "Filter",
        topBarActions = { Filter(model = model, screenModel = screenModel) },
        items = model.players,
        onClick = { screenModel.setPlayer(it.id) },
        columns = listOf(
            LazyTableColumn.Text(
                name = "Name",
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = "Team",
                weight = 2.0f,
                text = { it.teamName }
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
    Text("Filter out already in round")

    TextField(
        modifier = Modifier.padding(horizontal = 5.dp).width(TextFieldDefaults.MinWidth),
        value = model.nameFilter,
        onValueChange = screenModel::changeNameFilter,
        label = { Text("Filter by name") },
        singleLine = true
    )

    DropDownMenu(
        modifier = Modifier.width(TextFieldDefaults.MinWidth),
        label = "Team",
        items = Player.sortingOptions(),
        value = model.sortedBy,
        textMapper = Player.Sorting::text,
        onClick = screenModel::changeSorting
    )
}