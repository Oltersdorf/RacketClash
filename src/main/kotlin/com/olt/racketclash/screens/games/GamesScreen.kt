package com.olt.racketclash.screens.games

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Round
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

internal typealias editGame = (Long, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Boolean) -> Unit

class GamesScreen(private val modelBuilder: () -> GamesModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Games",
            topAppBarActions = {
                val navigator = LocalNavigator.currentOrThrow
                AddButton { screenModel.navigateTo(screen = Screens.NewRound, navigator = navigator) }
            },
            selectedTab = TournamentTabs.Games,
            navigateTo = screenModel::navigateTo
        ) {
            GamesView(paddingValues = it, rounds = stateModel.rounds, editGame = screenModel::updateGame, navigateTo = screenModel::navigateTo)
        }
    }
}

@Composable
private fun GamesView(
    paddingValues: PaddingValues,
    rounds: Map<Round, List<Game>>,
    editGame: editGame,
    navigateTo: (Screens, Navigator) -> Unit
) {
    Column(
        modifier = Modifier.padding(paddingValues = paddingValues).padding(5.dp)
    ) {
        Header()
        Graph(rounds = rounds, editGame = editGame, navigateTo = navigateTo)
    }
}

@Composable
private fun Header() {
    Surface(
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fields:",
                modifier = Modifier.padding(start = 10.dp)
            )
            FilledArrowLeftButton(enabled = true) {}
            Text("1")
            FilledArrowRightButton(enabled = true) {}
            Text(
                text = "Timeout:",
                modifier = Modifier.padding(start = 20.dp)
            )
            FilledArrowLeftButton(enabled = true) {}
            Text("1 min")
            FilledArrowRightButton(enabled = true) {}
        }
    }
}

@Composable
private fun Graph(
    rounds: Map<Round, List<Game>>,
    editGame: editGame,
    navigateTo: (Screens, Navigator) -> Unit
) {
    val horizontalScrollState = rememberLazyListState()
    val verticalScrollState = rememberScrollState()

    Box {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.verticalScroll(verticalScrollState).padding(20.dp),
            state = horizontalScrollState
        ) {
            items(items = rounds.keys.toList()) {
                Round(round = it, games = rounds[it] ?: emptyList(), editGame = editGame, navigateTo = navigateTo)
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = verticalScrollState),
            style = LocalScrollbarStyle.current.copy(hoverColor = MaterialTheme.colorScheme.secondary, unhoverColor = MaterialTheme.colorScheme.secondary)
        )

        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            adapter = rememberScrollbarAdapter(scrollState = horizontalScrollState),
            style = LocalScrollbarStyle.current.copy(hoverColor = MaterialTheme.colorScheme.secondary, unhoverColor = MaterialTheme.colorScheme.secondary)
        )
    }
}

@Composable
private fun Round(
    round: Round,
    games: List<Game>,
    editGame: editGame,
    navigateTo: (Screens, Navigator) -> Unit
) {
    Box(
        modifier = Modifier
            .border(width = 4.dp, color = MaterialTheme.colorScheme.primaryContainer)
            .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
            .width(400.dp)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.padding(start = 6.dp), text = round.name)
                Spacer(modifier = Modifier.weight(1.0f))
                val navigator = LocalNavigator.currentOrThrow
                EditButton { navigateTo(Screens.EditRound(round = round), navigator) }
            }
            if (games.isEmpty())
                Text(modifier = Modifier.padding(start = 6.dp), text = "No games available")
            else
                games.forEach {
                    RoundItem(game = it, editGame = editGame)
                }
        }
    }
}

@Composable
private fun RoundItem(
    game: Game,
    editGame: editGame
) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .border(width = 2.dp, color = MaterialTheme.colorScheme.primaryContainer)
            .background(color = MaterialTheme.colorScheme.tertiary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            var pointsLeft by remember { mutableStateOf(game.set1Left.toString()) }
            var pointsRight by remember { mutableStateOf(game.set1Right.toString()) }

            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = game.playerLeft1Name ?: "")
                        Text(text = game.playerLeft2Name ?: "")
                    }
                    BasicTextField(
                        value = pointsLeft,
                        onValueChange = {
                            val p = it.toIntOrNull()
                            if (it.isEmpty() || (p != null && p > 0))
                                pointsLeft = it
                        },
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.primary).widthIn(max = 20.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = game.playerRight1Name ?: "")
                        Text(text = game.playerRight2Name ?: "")
                    }
                    BasicTextField(
                        value = pointsRight,
                        onValueChange = {
                            val p = it.toIntOrNull()
                            if (it.isEmpty() || (p != null && p > 0))
                                pointsRight = it
                        },
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.primary).widthIn(max = 20.dp)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                SaveButton(enabled = !game.isDone) {
                    editGame(game.id, pointsLeft.toInt(), pointsRight.toInt(), 0, 0, 0, 0, 0, 0, 0, 0, true)
                }
                EditButton(enabled = game.isDone) {
                    editGame(game.id, pointsLeft.toInt(), pointsRight.toInt(), 0, 0, 0, 0, 0, 0, 0, 0, false)
                }
            }
        }
    }
}