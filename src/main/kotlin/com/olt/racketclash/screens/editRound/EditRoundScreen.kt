package com.olt.racketclash.screens.editRound

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class EditRoundScreen(private val modelBuilder: () -> EditRoundModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Edit Round",
            hasBackPress = true,
            selectedTab = TournamentTabs.Games,
            navigateTo = screenModel::navigateTo
        ) {
            SettingsView {
                EditRoundView(
                    model = stateModel,
                    screenModel = screenModel
                )
            }
        }
    }
}

@Composable
private fun EditRoundView(
    model: EditRoundModel.Model,
    screenModel: EditRoundModel
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = model.temporaryRoundName,
        onValueChange = screenModel::updateTemporaryRoundName,
        label = { Text("Name") },
        trailingIcon = {
            SaveButton(
                onClick = screenModel::saveRoundName,
                enabled = model.temporaryRoundName != model.round?.name
            )
        }
    )

    val navigator = LocalNavigator.currentOrThrow
    LazyTableWithScrollScaffold(
        modifier = Modifier.requiredHeightIn(max = 500.dp),
        topBarTitle = "Games",
        topBarActions = { AddButton { screenModel.navigateTo(Screens.EditGame(roundId = model.round?.id ?: -1), navigator) } },
        items = model.games,
        columns = listOf(
            LazyTableColumn.Builder(
                name = "Left",
                weight = 5.0f,
                headerTextAlign = TextAlign.Center
            ) { item, weight ->
                Column(
                    modifier = Modifier.weight(weight),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(item.playerLeft1Name ?: "<Empty>")
                    Text(item.playerLeft2Name ?: "<Empty>")
                }
            },
            LazyTableColumn.Builder(
                name = "Results",
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
                        Text("pending")
                }
            },
            LazyTableColumn.Builder(
                name = "Right",
                weight = 5.0f,
                headerTextAlign = TextAlign.Center
            ) { item, weight ->
                Column(
                    modifier = Modifier.weight(weight),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(item.playerRight1Name ?: "<Empty>")
                    Text(item.playerRight2Name ?: "<Empty>")
                }
            },
            LazyTableColumn.IconButton(
                name = "Delete",
                weight = 1.0f,
                headerTextAlign = TextAlign.Center,
                onClick = { screenModel.deleteGame(gameId = it.id) },
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        )
    )
}