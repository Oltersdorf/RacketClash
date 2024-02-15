package com.olt.racketclash.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import com.olt.racketclash.model.PlayerModel

internal typealias updatePlayer = (id: Long?, name: String, teamId: Long) -> Unit
internal typealias updateActive = (id: Long, active: Boolean) -> Unit

class PlayerScreen(private val database: Database) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { PlayerModel(database = database) }
        val modal by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Player",
            topAppBarActions = {
                val navigator = LocalNavigator.currentOrThrow

                IconButton(onClick = { navigator.push(EditPlayerScreen(player = null, teams = modal.teams, updatePlayer = screenModel::updatePlayer)) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
            selectedTab = TournamentTabs.Player
        ) {
            PlayerView(paddingValues = it, modal = modal, updatePlayer = screenModel::updatePlayer, updateActive = screenModel::updateActive)
        }
    }
}

@Composable
private fun PlayerView(
    paddingValues: PaddingValues,
    modal: PlayerModel.Modal,
    updatePlayer: updatePlayer,
    updateActive: updateActive
) {
    if (modal.isLoading)
        Loading(paddingValues = paddingValues)
    else
        PlayerList(paddingValues = paddingValues, player = modal.player, teams = modal.teams, updatePlayer = updatePlayer, updateActive = updateActive)
}

@Composable
private fun PlayerList(
    paddingValues: PaddingValues,
    player: List<Player>,
    teams: List<Team>,
    updatePlayer: updatePlayer,
    updateActive: updateActive
) {
    LazyColumnWithScroll(
        modifier = Modifier.padding(paddingValues = paddingValues).padding(5.dp),
        header = {
            Surface(
                tonalElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Active",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "Name",
                        modifier = Modifier.weight(5.0f)
                    )
                    Text(
                        text = "Team",
                        modifier = Modifier.weight(2.0f)
                    )
                    Text(
                        text = "played",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "bye",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "Games",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "Sets",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "Points",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "Edit",
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    ) {
        items(items = player) {player ->
            val navigator = LocalNavigator.currentOrThrow

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = player.active,
                    onCheckedChange = { updateActive(player.id, it) },
                    modifier = Modifier.weight(1.0f)
                )
                Text(
                    text = player.name,
                    modifier = Modifier.weight(5.0f)
                )
                Text(
                    text = player.teamName,
                    modifier = Modifier.weight(2.0f)
                )
                Text(
                    text = player.played.toString(),
                    modifier = Modifier.weight(1.0f)
                )
                Text(
                    text = player.bye.toString(),
                    modifier = Modifier.weight(1.0f)
                )
                Text(
                    text = "${player.games.first} : ${player.games.second}",
                    modifier = Modifier.weight(1.0f)
                )
                Text(
                    text = "${player.sets.first} : ${player.sets.second}",
                    modifier = Modifier.weight(1.0f)
                )
                Text(
                    text = "${player.points.first} : ${player.points.second}",
                    modifier = Modifier.weight(1.0f)
                )
                IconButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = { navigator.push(EditPlayerScreen(player = player, teams = teams, updatePlayer = updatePlayer)) }
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