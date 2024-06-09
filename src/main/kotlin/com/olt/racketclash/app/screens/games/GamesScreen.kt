package com.olt.racketclash.app.screens.games

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.olt.racketclash.data.Bye
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Round
import com.olt.racketclash.language.Language
import com.olt.racketclash.app.Screens
import com.olt.racketclash.ui.*

@Composable
fun GamesScreen(
    model: GamesModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    TournamentScaffold(
        language = language,
        topAppBarTitle = language.games,
        topAppBarActions = {
            AddButton {
                navigateTo(Screens.NewRound())
            }
        },
        selectedTab = TournamentTabs.Games(language = language),
        navigateTo = { navigateTo(it) }
    ) {
        GamesView(state = state, model = model, language = language, navigateTo = navigateTo)
    }
}

@Composable
private fun GamesView(
    state: GamesModel.State,
    model: GamesModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    Column {
        Header(state = state, model = model, language = language)
        Graph(state = state, model = model, language = language, navigateTo = navigateTo)
    }
}

@Composable
private fun Header(
    state: GamesModel.State,
    model: GamesModel,
    language: Language
) {
    Surface(tonalElevation = 5.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            NumberSelector(
                modifier = Modifier.padding(start = 10.dp),
                label = language.fields + ":",
                value = state.fields,
                onValueChange = model::changeFields,
                min = 1
            )
            NumberSelector(
                label = language.timeout + ":",
                value = state.timeout,
                valuePostText = language.minutes,
                onValueChange = model::changeTimeout,
                min = 1
            )

            Spacer(modifier = Modifier.weight(1.0f))

            Text(language.pointsForBye + ":")
            NumberSelector(
                label = language.games,
                value = state.gamePointsForBye,
                onValueChange = model::changeGamePointsForBye,
                min = 0
            )
            NumberSelector(
                label = language.sets,
                value = state.setPointsForBye,
                onValueChange = model::changeSetPointsForBye,
                min = 0
            )
            NumberSelector(
                label = language.points,
                value = state.pointsForBye,
                onValueChange = model::changePointsForBye,
                min = 0
            )
        }
    }
}

@Composable
private fun Graph(
    state: GamesModel.State,
    model: GamesModel,
    language: Language,
    navigateTo: (Screens) -> Unit
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
                items(items = state.rounds) {
                    Round(
                        language = language,
                        round = it,
                        games = state.games[it.id] ?: emptyList(),
                        bye = state.bye[it.id] ?: emptyList(),
                        active = state.active,
                        model = model,
                        navigateTo = navigateTo
                    )
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
    language: Language,
    round: Round,
    games: List<Game>,
    bye: List<Bye>,
    active: List<Long>,
    model: GamesModel,
    navigateTo: (Screens) -> Unit
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
                EditButton { navigateTo(Screens.EditRound(roundId = round.id)) }
                DeleteButton(enabled = games.isEmpty() && bye.isEmpty()) { model.deleteRound(id = round.id) }
            }

            if (bye.isNotEmpty()) ByeItem(language = language, byes = bye)

            if (games.isEmpty())
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp).fillMaxWidth(),
                    text = language.noGamesAvailable,
                    textAlign = TextAlign.Center
                )
            else
                games.forEachIndexed { index, game ->
                    GameItem(
                        language = language,
                        isFirst = bye.isEmpty() && index == 0,
                        active = active.contains(game.id),
                        game = game,
                        model = model
                    )
                }
        }
    }
}

@Composable
private fun ByeItem(
    language: Language,
    byes: List<Bye>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .border(width = 2.dp, color = MaterialTheme.colorScheme.primaryContainer)
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 5.dp),
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleLarge, text = language.byes
        )
        byes.forEachIndexed { index, bye ->
            Text(
                modifier = Modifier.padding(bottom = if (index + 1 == byes.size) 5.dp else 0.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                text = "${bye.playerName} (${bye.playerTeamName})"
            )
        }
    }
}

@Composable
private fun GameItem(
    language: Language,
    isFirst: Boolean,
    active: Boolean,
    game: Game,
    model: GamesModel
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
            SelectionContainer {
                Text(color = textColor, text = "${game.playerLeft1Name ?: "<${language.empty}>"} (${game.playerLeft1TeamName})")
            }
            SelectionContainer {
                Text(color = textColor, text = "${game.playerLeft2Name ?: "<${language.empty}>"} (${game.playerLeft2TeamName})")
            }
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
                PendingSet(setNumber = 1, setLeft = game.set1Left, setRight = game.set1Right, game = game, model = model)
                PendingSet(setNumber = 2, setLeft = game.set2Left, setRight = game.set2Right, game = game, model = model)
                PendingSet(setNumber = 3, setLeft = game.set3Left, setRight = game.set3Right, game = game, model = model)
                PendingSet(setNumber = 4, setLeft = game.set4Left, setRight = game.set4Right, game = game, model = model)
                PendingSet(setNumber = 5, setLeft = game.set5Left, setRight = game.set5Right, game = game, model = model)
            }
        }

        Column(
            modifier = Modifier.weight(1.0f).padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelectionContainer {
                Text(color = textColor, text = "${game.playerRight1Name ?: "<${language.empty}>"} (${game.playerRight1TeamName})")
            }
            SelectionContainer {
                Text(color = textColor, text = "${game.playerRight2Name ?: "<${language.empty}>"} (${game.playerRight2TeamName})")
            }
        }

        if (game.isDone)
            EditButton(
                modifier = Modifier.padding(vertical = 5.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = textColor)
            ) { model.setDone(game = game, isDone = false) }
        else
            SaveButton(
                modifier = Modifier.padding(vertical = 5.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = textColor)
            ) { model.setDone(game = game, isDone = true) }
    }
}

@Composable
private fun PendingSet(
    setNumber: Int,
    setLeft: Int,
    setRight: Int,
    game: Game,
    model: GamesModel
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
                model.changeSet(
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
                model.changeSet(
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
            color = MaterialTheme.colorScheme.onSecondary,
            text = setLeft.toString(),
            textAlign = TextAlign.End,
            softWrap = false
        )
        Text(color = MaterialTheme.colorScheme.onSecondary, text = " : ")
        Text(
            modifier = Modifier.weight(1.0f),
            color = MaterialTheme.colorScheme.onSecondary,
            text = setRight.toString(),
            softWrap = false
        )
    }
}