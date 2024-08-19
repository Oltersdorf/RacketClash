package com.olt.racketclash

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.olt.racketclash.editRound.EditRoundModel
import com.olt.racketclash.language.Language
import com.olt.racketclash.navigate.Screens
import com.olt.racketclash.ui.*

@Composable
fun EditRoundScreen(
    model: EditRoundModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    TournamentScaffold(
        language = language,
        projectId = state.projectId,
        topAppBarTitle = language.editRound,
        hasBackPress = true,
        selectedTab = TournamentTabs.Games(language = language, projectId = state.projectId),
        navigateTo = { navigateTo(it) }
    ) {
        SettingsView {
            EditRoundView(
                state = state,
                model = model,
                language = language,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
private fun EditRoundView(
    state: EditRoundModel.State,
    model: EditRoundModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.temporaryRoundName,
        onValueChange = model::updateTemporaryRoundName,
        label = { Text(language.name) },
        trailingIcon = {
            SaveButton(
                onClick = model::saveRoundName,
                enabled = state.temporaryRoundName != state.round?.name
            )
        }
    )

    LazyTableWithScrollScaffold(
        topBarTitle = language.games,
        topBarActions = {
            AddButton {
                navigateTo(Screens.EditGame(roundId = state.round?.id ?: -1, projectId = state.projectId))
            }
        },
        items = state.games,
        columns = listOf(
            LazyTableColumn.Builder(
                name = language.left,
                weight = 5.0f,
                headerTextAlign = TextAlign.Center
            ) { item, weight ->
                Column(
                    modifier = Modifier.weight(weight),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${item.playerLeft1Name ?: "<${language.empty}>"} (${item.playerLeft1TeamName})")
                    Text("${item.playerLeft2Name ?: "<${language.empty}>"} (${item.playerLeft2TeamName})")
                }
            },
            LazyTableColumn.Builder(
                name = language.results,
                weight = 1.0f,
                headerTextAlign = TextAlign.Center
            ) { item, weight ->
                Column(
                    modifier = Modifier.weight(weight),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (item.isDone) {
                        if (item.set1Left + item.set1Right > 0)
                            Text("${item.set1Left} : ${item.set1Right}")
                        if (item.set2Left + item.set2Right > 0)
                            Text("${item.set2Left} : ${item.set2Right}")
                        if (item.set3Left + item.set3Right > 0)
                            Text("${item.set3Left} : ${item.set3Right}")
                        if (item.set4Left + item.set4Right > 0)
                            Text("${item.set4Left} : ${item.set4Right}")
                        if (item.set5Left + item.set5Right > 0)
                            Text("${item.set5Left} : ${item.set5Right}")
                    } else
                        Text(language.pending)
                }
            },
            LazyTableColumn.Builder(
                name = language.right,
                weight = 5.0f,
                headerTextAlign = TextAlign.Center
            ) { item, weight ->
                Column(
                    modifier = Modifier.weight(weight),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${item.playerRight1Name ?: "<${language.empty}>"} (${item.playerRight1TeamName})")
                    Text("${item.playerRight2Name ?: "<${language.empty}>"} (${item.playerRight2TeamName})")
                }
            },
            LazyTableColumn.IconButton(
                name = language.delete,
                weight = 1.0f,
                headerTextAlign = TextAlign.Center,
                onClick = { model.deleteGame(gameId = it.id) },
                imageVector = Icons.Default.Delete,
                contentDescription = language.delete
            )
        )
    )

    LazyTableWithScrollScaffold(
        topBarTitle = language.byes,
        topBarActions = {
            AddButton {
                navigateTo(Screens.EditGame(roundId = state.round?.id ?: -1, projectId = state.projectId))
            }
        },
        items = state.byes,
        columns = listOf(
            LazyTableColumn.Text(
                name = language.name,
                weight = 5.0f
            ) {
                "${it.playerName ?: "<${language.empty}>"} (${it.playerTeamName})"
            },
            LazyTableColumn.IconButton(
                name = language.delete,
                weight = 1.0f,
                headerTextAlign = TextAlign.Center,
                onClick = { model.deleteBye(byeId = it.id) },
                imageVector = Icons.Default.Delete,
                contentDescription = language.delete
            )
        )
    )
}