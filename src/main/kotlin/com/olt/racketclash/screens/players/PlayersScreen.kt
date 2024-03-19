package com.olt.racketclash.screens.players

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Player
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

internal typealias updateActive = (id: Long, active: Boolean) -> Unit

class PlayersScreen(private val modelBuilder: () -> PlayersModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Player",
            topAppBarActions = {
                val navigator = LocalNavigator.currentOrThrow

                IconButton(onClick = { screenModel.navigateTo(Screens.EditPlayer(player = null), navigator) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
            selectedTab = TournamentTabs.Players,
            navigateTo = screenModel::navigateTo
        ) {
            PlayerView(paddingValues = it, model = stateModel, updateActive = screenModel::updateActive, navigateTo = screenModel::navigateTo)
        }
    }
}

@Composable
private fun PlayerView(
    paddingValues: PaddingValues,
    model: PlayersModel.Modal,
    updateActive: updateActive,
    navigateTo: (Screens, Navigator) -> Unit
) {
    if (model.isLoading)
        Loading(paddingValues = paddingValues)
    else
        PlayerList(paddingValues = paddingValues, player = model.players, updateActive = updateActive, navigateTo = navigateTo)
}

@Composable
private fun PlayerList(
    paddingValues: PaddingValues,
    player: List<Player>,
    updateActive: updateActive,
    navigateTo: (Screens, Navigator) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    LazyTableWithScroll(
        items = player,
        modifier = Modifier.padding(paddingValues = paddingValues).padding(5.dp),
        onClick = { navigateTo(Screens.EditPlayer(it), navigator) },
        columns = listOf(
            LazyTableColumn(
                name = "Active",
                weight = 1.0f
            ) {item, weight ->
                Checkbox(
                    checked = item.active,
                    onCheckedChange = { checked -> updateActive(item.id, checked) },
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Name",
                weight = 5.0f
            ) {item, weight ->
                Text(
                    text = item.name,
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Team",
                weight = 2.0f
            ) { item, weight ->
                Text(
                    text = item.teamName,
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "played",
                weight = 1.0f
            ) { item, weight ->
                Text(
                    text = item.played.toString(),
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "bye",
                weight = 1.0f
            ) { item, weight ->
                Text(
                    text = item.bye.toString(),
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Games",
                weight = 1.0f
            ) { item, weight ->
                Text(
                    text = "${item.games.first} : ${item.games.second}",
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Sets",
                weight = 1.0f
            ) { item, weight ->
                Text(
                    text = "${item.sets.first} : ${item.sets.second}",
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Points",
                weight = 1.0f
            ) { item, weight ->
                Text(
                    text = "${item.points.first} : ${item.points.second}",
                    modifier = Modifier.weight(weight)
                )
            },
            LazyTableColumn(
                name = "Edit",
                weight = 0.5f,
                textAlign = TextAlign.Center
            ) { item, weight ->
                IconButton(
                    modifier = Modifier.weight(weight),
                    onClick = { navigateTo(Screens.EditPlayer(player = item), navigator) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
        )
    )
}