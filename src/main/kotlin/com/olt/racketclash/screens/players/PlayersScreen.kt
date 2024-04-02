package com.olt.racketclash.screens.players

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
                AddButton { screenModel.navigateTo(Screens.EditPlayer(player = null), navigator) }
            },
            selectedTab = TournamentTabs.Players,
            navigateTo = screenModel::navigateTo
        ) {
            PlayerView(
                model = stateModel,
                updateActive = screenModel::updateActive,
                deletePlayer = screenModel::deletePlayer,
                navigateTo = screenModel::navigateTo
            )
        }
    }
}

@Composable
private fun PlayerView(
    model: PlayersModel.Modal,
    updateActive: updateActive,
    deletePlayer: (Long) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    if (model.isLoading)
        Loading()
    else
        PlayerList(
            player = model.players,
            updateActive = updateActive,
            deletePlayer = deletePlayer,
            navigateTo = navigateTo
        )
}

@Composable
private fun PlayerList(
    player: List<Player>,
    updateActive: updateActive,
    deletePlayer: (Long) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    LazyTableWithScroll(
        items = player,
        modifier = Modifier.padding(5.dp),
        onClick = { navigateTo(Screens.EditPlayer(it), navigator) },
        columns = listOf(
            LazyTableColumn.Checkbox(
                name = "Active",
                weight = 1.0f,
                checked = { it.active },
                onCheckChanged = { item, checked -> updateActive(item.id, checked) }
            ),
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
                text = { "${it.games.first} : ${it.games.second}" }
            ),
            LazyTableColumn.Text(
                name = "Sets",
                weight = 1.0f,
                text = { "${it.sets.first} : ${it.sets.second}" }
            ),
            LazyTableColumn.Text(
                name = "Points",
                weight = 1.0f,
                text = { "${it.points.first} : ${it.points.second}" }
            ),
            LazyTableColumn.IconButton(
                name = "Delete",
                weight = 0.5f,
                headerTextAlign = TextAlign.Center,
                onClick = { deletePlayer(it.id) },
                enabled = { it.played == 0 },
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        )
    )
}