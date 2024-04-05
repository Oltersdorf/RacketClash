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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Round
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

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
            GamesView(model = stateModel, screenModel = screenModel)
        }
    }
}

@Composable
private fun GamesView(
    model: GamesModel.Model,
    screenModel: GamesModel
) {
    Column {
        Header(model = model, screenModel = screenModel)
        Graph(model = model, screenModel = screenModel)
    }
}

@Composable
private fun Header(
    model: GamesModel.Model,
    screenModel: GamesModel
) {
    Surface(tonalElevation = 5.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fields:",
                modifier = Modifier.padding(start = 10.dp)
            )
            FilledArrowLeftButton(enabled = model.canSubtractFields) { screenModel.changeFields(newFields = model.fields - 1) }
            Text(model.fields.toString())
            FilledArrowRightButton { screenModel.changeFields(newFields = model.fields + 1) }
            Text(
                text = "Timeout:",
                modifier = Modifier.padding(start = 20.dp)
            )
            FilledArrowLeftButton(enabled = model.canSubtractTimeout) { screenModel.changeTimeout(newTimeout = model.timeout - 1) }
            Text("${model.timeout} min")
            FilledArrowRightButton { screenModel.changeTimeout(newTimeout = model.timeout + 1) }
        }
    }
}

@Composable
private fun Graph(
    model: GamesModel.Model,
    screenModel: GamesModel
) {
    Box {
        val horizontalScrollState = rememberLazyListState()
        val verticalScrollState = rememberScrollState()

        Box(modifier = Modifier.padding(20.dp)) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.verticalScroll(verticalScrollState),
                state = horizontalScrollState
            ) {
                items(items = model.rounds) {
                    Round(round = it, games = model.games[it.id] ?: emptyList(), active = model.active, screenModel = screenModel)
                }
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
    active: List<Long>,
    screenModel: GamesModel
) {
    Box(
        modifier = Modifier
            .border(width = 4.dp, color = MaterialTheme.colorScheme.primaryContainer)
            .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp))
            .width(600.dp)
            .padding(4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(round.name)
                Spacer(modifier = Modifier.weight(1.0f))
                val navigator = LocalNavigator.currentOrThrow
                EditButton { screenModel.navigateTo(Screens.EditRound(round = round), navigator) }
                DeleteButton(enabled = games.isEmpty()) { screenModel.deleteRound(id = round.id) }
            }

            if (games.isEmpty())
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp).fillMaxWidth(),
                    text = "No games available",
                    textAlign = TextAlign.Center
                )
            else
                games.forEachIndexed { index, game ->
                    RoundItem(isFirst = index == 0, active = active.contains(game.id), game = game, screenModel = screenModel)
                }
        }
    }
}

@Composable
private fun RoundItem(
    isFirst: Boolean,
    active: Boolean,
    game: Game,
    screenModel: GamesModel
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp, top = if (isFirst) 5.dp else 0.dp)
            .border(width = 2.dp, color = MaterialTheme.colorScheme.primaryContainer)
            .background(color = if (active) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val textColor = if (active) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSecondary

        Column(
            modifier = Modifier.weight(1.0f).padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(color = textColor, text = game.playerLeft1Name ?: "<Empty>")
            Text(color = textColor, text = game.playerLeft2Name ?: "<Empty>")
        }

        Column(
            modifier = Modifier.padding(vertical = 5.dp).width(70.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (game.isDone) {
                DoneSet(setLeft = game.set1Left, setRight = game.set1Right)
                DoneSet(setLeft = game.set2Left, setRight = game.set2Right)
                DoneSet(setLeft = game.set3Left, setRight = game.set3Right)
                DoneSet(setLeft = game.set4Left, setRight = game.set4Right)
                DoneSet(setLeft = game.set5Left, setRight = game.set5Right)
            } else {
                PendingSet(setNumber = 1, setLeft = game.set1Left, setRight = game.set1Right, game = game, screenModel = screenModel)
                PendingSet(setNumber = 2, setLeft = game.set2Left, setRight = game.set2Right, game = game, screenModel = screenModel)
                PendingSet(setNumber = 3, setLeft = game.set3Left, setRight = game.set3Right, game = game, screenModel = screenModel)
                PendingSet(setNumber = 4, setLeft = game.set4Left, setRight = game.set4Right, game = game, screenModel = screenModel)
                PendingSet(setNumber = 5, setLeft = game.set5Left, setRight = game.set5Right, game = game, screenModel = screenModel)
            }
        }

        Column(
            modifier = Modifier.weight(1.0f).padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(color = textColor, text = game.playerRight1Name ?: "<Empty>")
            Text(color = textColor, text = game.playerRight2Name ?: "<Empty>")
        }

        if (game.isDone)
            EditButton(modifier = Modifier.padding(vertical = 5.dp)) { screenModel.setDone(game = game, isDone = false) }
        else
            SaveButton(modifier = Modifier.padding(vertical = 5.dp)) { screenModel.setDone(game = game, isDone = true) }
    }
}

@Composable
private fun PendingSet(
    setNumber: Int,
    setLeft: Int,
    setRight: Int,
    game: Game,
    screenModel: GamesModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .weight(1.0f),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSecondaryContainer, textAlign = TextAlign.End),
            value = setLeft.toString(),
            onValueChange = {
                screenModel.changeSet(
                    roundId = game.roundId,
                    gameId = game.id,
                    setNumber = setNumber,
                    isLeft = true,
                    text = it)
            }
        )

        Text(color = MaterialTheme.colorScheme.onError, text = " : ")

        BasicTextField(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .weight(1.0f),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
            value = setRight.toString(),
            onValueChange = {
                screenModel.changeSet(
                    roundId = game.roundId,
                    gameId = game.id,
                    setNumber = setNumber,
                    isLeft = false,
                    text = it)
            }
        )
    }
}

@Composable
private fun DoneSet(
    setLeft: Int,
    setRight: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            color = MaterialTheme.colorScheme.onTertiary,
            text = setLeft.toString(),
            textAlign = TextAlign.End,
            softWrap = false
        )
        Text(color = MaterialTheme.colorScheme.onTertiary, text = " : ")
        Text(
            modifier = Modifier.weight(1.0f),
            color = MaterialTheme.colorScheme.onTertiary,
            text = setRight.toString(),
            softWrap = false
        )
    }
}