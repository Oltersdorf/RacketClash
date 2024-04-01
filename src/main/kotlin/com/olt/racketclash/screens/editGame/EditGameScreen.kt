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
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Team
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
            EditGameView(
                paddingValues = it,
                model = stateModel,
                selectedPlayer = screenModel::selectPlayer,
                removePlayer = screenModel::removePlayer,
                changeNameFilter = screenModel::changeNameFilter,
                changeTeamFilter = screenModel::changeTeamFilter,
                setPlayer = screenModel::setPlayer,
                addGame = screenModel::addGame,
                navigateTo = screenModel::navigateTo
            )
        }
    }
}

@Composable
private fun EditGameView(
    paddingValues: PaddingValues,
    model: EditGameModel.Model,
    selectedPlayer: (EditGameModel.SelectedPlayer) -> Unit,
    removePlayer: (EditGameModel.SelectedPlayer) -> Unit,
    changeNameFilter: (String) -> Unit,
    changeTeamFilter: (Team?) -> Unit,
    setPlayer: (Long) -> Unit,
    addGame: () -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)
    ) {
        Column(
            modifier = Modifier.requiredWidthIn(500.dp).fillMaxWidth(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = model.roundName, style = MaterialTheme.typography.titleLarge)

            Row(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        BorderText<EditGameModel.SelectedPlayer.Left1>(
                            selectedPlayer = model.selectedPlayer,
                            text = model.player1LeftDisplayName
                        )
                        if (model.player1Left == null)
                            EditButton { selectedPlayer(EditGameModel.SelectedPlayer.Left1) }
                        else
                            DeleteButton { removePlayer(EditGameModel.SelectedPlayer.Left1) }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BorderText<EditGameModel.SelectedPlayer.Left2>(
                            selectedPlayer = model.selectedPlayer,
                            text = model.player2LeftDisplayName
                        )
                        if (model.player2Left == null)
                            EditButton { selectedPlayer(EditGameModel.SelectedPlayer.Left2) }
                        else
                            DeleteButton{ removePlayer(EditGameModel.SelectedPlayer.Left2) }
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
                            EditButton { selectedPlayer(EditGameModel.SelectedPlayer.Right1) }
                        else
                            DeleteButton { removePlayer(EditGameModel.SelectedPlayer.Right1) }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        BorderText<EditGameModel.SelectedPlayer.Right2>(
                            selectedPlayer = model.selectedPlayer,
                            text = model.player2RightDisplayName
                        )
                        if (model.player2Right == null)
                            EditButton { selectedPlayer(EditGameModel.SelectedPlayer.Right2) }
                        else
                            DeleteButton { removePlayer(EditGameModel.SelectedPlayer.Right2) }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val navigator = LocalNavigator.currentOrThrow

                Spacer(modifier = Modifier.weight(1.0f))
                Button(onClick = { navigateTo(Screens.Pop, navigator) }) {
                    Text("Cancel")
                }
                Button(onClick = { addGame() }) {
                    Text("Save")
                }
            }

            Row {
                OutlinedTextField(
                    value = model.nameFilter,
                    onValueChange = changeNameFilter,
                    label = { Text("Filter by name") }
                )

                DropDownMenu(
                    modifier = Modifier,
                    items = model.teams,
                    value = model.teamFilter?.name ?: "<No team selected>",
                    textMapper = { it?.name ?: "<No team selected>" },
                    onClick = changeTeamFilter
                )
            }

            LazyTableWithScroll(
                items = model.players,
                onClick = { setPlayer(it.id) },
                columns = listOf(
                    LazyTableColumn.Text(name = "Name", weight = 5.0f, text = { it.name }),
                    LazyTableColumn.Text(name = "Team", weight = 2.0f, text = { it.teamName })
                )
            )
        }
    }
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