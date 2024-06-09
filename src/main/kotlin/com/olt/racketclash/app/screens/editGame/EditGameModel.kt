package com.olt.racketclash.app.screens.editGame

import com.olt.racketclash.data.*
import com.olt.racketclash.data.database.Database
import com.olt.racketclash.state.ViewModelState

class EditGameModel(
    private val database: Database,
    project: Project?,
    private val roundId: Long
) : ViewModelState<EditGameModel.State>(initialState = State()) {

    private var unfilteredPlayers: List<Player> = emptyList()
    private var games: List<Game> = emptyList()
    private var byes: List<Bye> = emptyList()

    init {
        onIO {
            database.round(id = roundId).collect {
                updateState { copy(roundName = it?.name ?: "") }
            }
        }

        onIO {
            database.bye(roundId = roundId).collect {
                byes = it
            }
        }

        onIO {
            database.players().collect { playerList ->
                val players = playerList.toMutableList()

                if (project != null)
                    players.replaceAll {
                        it.copy(
                            wonGames = it.wonGames + (project.gamePointsForBye * it.bye),
                            wonSets = it.wonSets + (project.setPointsForBye * it.bye),
                            wonPoints = it.wonPoints + (project.pointsForBye * it.bye)
                        )
                    }

                unfilteredPlayers = players.toList()
            }
        }

        onIO {
            database.games(roundId = roundId).collect {
                games = it
            }
        }
    }

    sealed class SelectedPlayer {
        data object Left1 : SelectedPlayer()
        data object Left2 : SelectedPlayer()
        data object Right1 : SelectedPlayer()
        data object Right2 : SelectedPlayer()
    }

    data class State(
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
        val filterNotInRound: Boolean = false,
        val nameFilter: String = "",
        val sortedBy: Player.Sorting = Player.Sorting.NameAscending,
        val isBye: Boolean = false
    )

    fun selectPlayer(selection: SelectedPlayer) =
        onDefault {
            updateState {
                copy(
                    selectedPlayer = selection,
                    players = filterAndSortPlayer(model = this)
                )
            }
        }

    fun setIsBye(newIsBye: Boolean) =
        onDefault {
            updateState { copy(isBye = newIsBye) }
        }

    fun setPlayer(playerId: Long) =
        onDefault {
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

    fun removePlayer(selection: SelectedPlayer) =
        onDefault {
            updateState {
                when (selection) {
                    SelectedPlayer.Left1 -> copy(player1Left = null, player1LeftDisplayName = "<empty>")
                    SelectedPlayer.Left2 -> copy(player2Left = null, player2LeftDisplayName = "<empty>")
                    SelectedPlayer.Right1 -> copy(player1Right = null, player1RightDisplayName = "<empty>")
                    SelectedPlayer.Right2 -> copy(player2Right = null, player2RightDisplayName = "<empty>")
                }
            }
        }

    fun addGame() =
        onDefault {
            updateState {
                if (isBye)
                    database.addBye(roundId = roundId, playerId = player1Left?.id)
                else
                    database.addGame(
                        roundId = roundId,
                        playerLeft1Id = player1Left?.id,
                        playerLeft2Id = player2Left?.id,
                        playerRight1Id = player1Right?.id,
                        playerRight2Id = player2Right?.id
                    )
                copy(
                    player1Left = null,
                    player1LeftDisplayName = "<empty>",
                    player2Left = null,
                    player2LeftDisplayName = "<empty>",
                    player1Right = null,
                    player1RightDisplayName = "<empty>",
                    player2Right = null,
                    player2RightDisplayName = "<empty>"
                )
            }
        }

    fun changeNameFilter(newNameFilter: String) =
        onDefault {
            updateState {
                copy(
                    nameFilter = newNameFilter,
                    players = if (selectedPlayer != null)
                        filterAndSortPlayer(copy(nameFilter = newNameFilter))
                    else emptyList()
                )
            }
        }

    fun changeSorting(newSorting: Player.Sorting) =
        onDefault {
            updateState {
                copy(
                    sortedBy = newSorting,
                    players = if (selectedPlayer != null)
                        filterAndSortPlayer(copy(sortedBy = newSorting))
                    else emptyList()
                )
            }
        }

    fun changeFilterNotInRound(filterNotInRound: Boolean) =
        onDefault {
            updateState {
                copy(
                    filterNotInRound = filterNotInRound,
                    players = if (selectedPlayer != null)
                        filterAndSortPlayer(copy(filterNotInRound = filterNotInRound))
                    else emptyList()
                )
            }
        }

    private fun filterAndSortPlayer(model: State): List<Player> =
        unfilteredPlayers
            .asSequence()
            .filter { it.active }
            .filterNot { player -> if (model.filterNotInRound) games.find { it.playerLeft1Id == player.id } != null else false }
            .filterNot { player -> if (model.filterNotInRound) games.find { it.playerLeft2Id == player.id } != null else false }
            .filterNot { player -> if (model.filterNotInRound) games.find { it.playerRight1Id == player.id } != null else false }
            .filterNot { player -> if (model.filterNotInRound) games.find { it.playerRight2Id == player.id } != null else false }
            .filterNot { player -> if (model.filterNotInRound) byes.find { it.playerId == player.id } != null else false }
            .filter { it.name.contains(model.nameFilter, ignoreCase = true) }
            .filterNot { it.id == model.player1Left?.id }
            .filterNot { it.id == model.player2Left?.id }
            .filterNot { it.id == model.player1Right?.id }
            .filterNot { it.id == model.player2Right?.id }
            .toList()
            .sort(model.sortedBy)
}