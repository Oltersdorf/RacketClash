package com.olt.racketclash.screens.editGame

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditGameModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database,
    private val roundId: Long
) : NavigableStateScreenModel<EditGameModel.Model>(navigateToScreen = navigateToScreen, initialState = Model()) {

    private var unfilteredPlayers: List<Player> = emptyList()

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.round(id = roundId).collect {
                updateState { copy(roundName = it?.name ?: "") }
            }
        }

        screenModelScope.launch(context = Dispatchers.IO) {
            database.players().collect {
                unfilteredPlayers = it
            }
        }

        screenModelScope.launch(context = Dispatchers.IO) {
            database.teams().collect {
                updateState {
                    copy(teams = listOf(null, *it.toTypedArray())
                    )
                }
            }
        }
    }

    sealed class SelectedPlayer {
        data object Left1 : SelectedPlayer()
        data object Left2 : SelectedPlayer()
        data object Right1 : SelectedPlayer()
        data object Right2 : SelectedPlayer()
    }

    data class Model(
        val roundName: String = "",
        val player1Left: Player? = null,
        val player1LeftDisplayName: String = "<empty>",
        val player2Left: Player? = null,
        val player2LeftDisplayName: String = "<empty>",
        val player1Right: Player? = null,
        val player1RightDisplayName: String = "<empty>",
        val player2Right: Player? = null,
        val player2RightDisplayName: String = "<empty>",
        val players: List<Player> = emptyList(),
        val selectedPlayer: SelectedPlayer? = null,
        val nameFilter: String = "",
        val teams: List<Team?> = emptyList(),
        val teamFilter: Team? = null
    )

    fun selectPlayer(selection: SelectedPlayer) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    selectedPlayer = selection,
                    players = filterPlayer(model = this)
                )
            }
        }
    }

    fun setPlayer(playerId: Long) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                val player = unfilteredPlayers.find { it.id == playerId }
                val displayName =
                    if (player == null) ""
                    else "${player.name} (${player.teamName})"

                val model = copy(players = emptyList(), selectedPlayer = null)

                when (selectedPlayer) {
                    SelectedPlayer.Left1 -> model.copy(player1Left = player, player1LeftDisplayName = displayName)
                    SelectedPlayer.Left2 -> model.copy(player2Left = player, player2LeftDisplayName = displayName)
                    SelectedPlayer.Right1 -> model.copy(player1Right = player, player1RightDisplayName = displayName)
                    SelectedPlayer.Right2 -> model.copy(player2Right = player, player2RightDisplayName = displayName)
                    null -> model
                }
            }
        }
    }

    fun removePlayer(selection: SelectedPlayer) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                when (selection) {
                    SelectedPlayer.Left1 -> copy(player1Left = null, player1LeftDisplayName = "<empty>")
                    SelectedPlayer.Left2 -> copy(player2Left = null, player2LeftDisplayName = "<empty>")
                    SelectedPlayer.Right1 -> copy(player1Right = null, player1RightDisplayName = "<empty>")
                    SelectedPlayer.Right2 -> copy(player2Right = null, player2RightDisplayName = "<empty>")
                }
            }
        }
    }

    fun addGame() {
        screenModelScope.launch(context = Dispatchers.IO) {
            updateState {
                database.addGame(
                    roundId = roundId,
                    playerLeft1Id = player1Left?.id,
                    playerLeft2Id = player2Left?.id,
                    playerRight1Id = player1Right?.id,
                    playerRight2Id = player2Right?.id
                )
                Model(roundName = roundName)
            }
        }
    }

    fun changeNameFilter(newNameFilter: String) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    nameFilter = newNameFilter,
                    players = if (selectedPlayer != null)
                        filterPlayer(copy(nameFilter = newNameFilter))
                    else emptyList()
                )
            }
        }
    }

    fun changeTeamFilter(newTeamFilter: Team?) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    teamFilter = newTeamFilter,
                    players = if (selectedPlayer != null)
                        filterPlayer(copy(teamFilter = newTeamFilter))
                    else emptyList()
                )
            }
        }
    }

    private fun filterPlayer(model: Model): List<Player> =
        unfilteredPlayers
            .asSequence()
            .filter { it.active }
            .filter { if (model.teamFilter != null) it.teamId == model.teamFilter.id else true }
            .filter { it.name.contains(model.nameFilter, ignoreCase = true) }
            .filterNot { it.id == model.player1Left?.id }
            .filterNot { it.id == model.player2Left?.id }
            .filterNot { it.id == model.player1Right?.id }
            .filterNot { it.id == model.player2Right?.id }
            .toList()
}