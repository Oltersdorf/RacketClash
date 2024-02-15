package com.olt.racketclash.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Game
import com.olt.racketclash.model.RoundsModel

internal typealias editGame = (id: Long, set1Left: Int, set1Right: Int, isDone: Boolean) -> Unit
internal typealias deleteRound = (roundName: String) -> Unit

class RoundsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberDIModelOrThrow<RoundsModel>()
        val model by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Games",
            topAppBarActions = {
                val navigator = LocalNavigator.currentOrThrow

                IconButton(onClick = { navigator.push(EditRoundsScreen()) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
            selectedTab = TournamentTabs.Games
        ) {
            GamesView(paddingValues = it, games = model.games, editGame = screenModel::updateGame, deleteRound = screenModel::deleteRound)
        }
    }
}

@Composable
private fun GamesView(
    paddingValues: PaddingValues,
    games: Map<Int, List<Game>>,
    editGame: editGame,
    deleteRound: deleteRound
) {
    Column(
        modifier = Modifier.padding(paddingValues = paddingValues).padding(5.dp)
    ) {
        Header()
        Graph(games = games, editGame = editGame, deleteRound = deleteRound)
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
            FilledIconButton(
                onClick = {},
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Remove"
                )
            }
            Text("1")
            FilledIconButton(
                onClick = {},
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Add"
                )
            }
            Text(
                text = "Timeout:",
                modifier = Modifier.padding(start = 20.dp)
            )
            FilledIconButton(
                onClick = {},
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Remove"
                )
            }
            Text("1 min")
            FilledIconButton(
                onClick = {},
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Add"
                )
            }
        }
    }
}

@Composable
private fun Graph(
    games: Map<Int, List<Game>>,
    editGame: editGame,
    deleteRound: deleteRound
) {
    val horizontalScrollState = rememberLazyListState()
    val verticalScrollState = rememberScrollState()

    Box {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.verticalScroll(verticalScrollState).padding(20.dp),
            state = horizontalScrollState
        ) {
            items(items = games.values.toList()) {
                Round(games = it, editGame = editGame, deleteRound = deleteRound)
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
    games: List<Game>,
    editGame: editGame,
    deleteRound: deleteRound
) {
    Box(
        modifier = Modifier
            .border(width = 4.dp, color = MaterialTheme.colorScheme.primaryContainer)
            .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = games.first().roundName)
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(
                    onClick = { deleteRound(games.first().roundName) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            repeat(times = games.size - 1) {
                RoundItem(game = games[it], editGame = editGame)
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
                IconButton(
                    onClick = { editGame(game.id, pointsLeft.toInt(), pointsRight.toInt(), true) },
                    enabled = !game.isDone
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save"
                    )
                }
                IconButton(
                    onClick = { editGame(game.id, pointsLeft.toInt(), pointsRight.toInt(), false) },
                    enabled = game.isDone
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
        }
    }
}