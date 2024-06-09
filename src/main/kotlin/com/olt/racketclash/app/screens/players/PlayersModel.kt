package com.olt.racketclash.app.screens.players

import com.olt.racketclash.data.database.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Project
import com.olt.racketclash.data.sort
import com.olt.racketclash.state.ViewModelState

class PlayersModel(
    private val database: Database,
    project: Project?
) : ViewModelState<PlayersModel.State>(initialState = State()) {

    private var completePlayers: List<Player> = emptyList()

    init {
        onIO {
            database.players().collect { playerList ->
                updateState {
                    val players = playerList.toMutableList()

                    if (project != null)
                        players.replaceAll {
                            it.copy(
                                wonGames = it.wonGames + (project.gamePointsForBye * it.bye),
                                wonSets = it.wonSets + (project.setPointsForBye * it.bye),
                                wonPoints = it.wonPoints + (project.pointsForBye * it.bye)
                            )
                        }

                    completePlayers = players.toList()
                    copy(
                        isLoading = false,
                        players = players.toList().filter { it.name.contains(filter) }.sort(sortedBy = sortedBy)
                    )
                }
            }
        }
    }

    data class State(
        val isLoading: Boolean = true,
        val players: List<Player> = emptyList(),
        val filter: String = "",
        val sortedBy: Player.Sorting = Player.Sorting.NameAscending
    )

    fun updateActive(id: Long, active: Boolean) =
        onIO {
            database.playerSetActive(id = id, active = active)
        }

    fun deletePlayer(id: Long) =
        onIO {
            database.deletePlayer(id = id)
        }

    fun changeFilter(newFilter: String) =
        onDefault {
            updateState {
                copy(
                    filter = newFilter,
                    players = completePlayers.filter { it.name.contains(newFilter) }.sort(sortedBy = sortedBy)
                )
            }
        }

    fun changeSorting(newSorting: Player.Sorting) =
        onDefault {
            updateState {
                copy(
                    sortedBy = newSorting,
                    players = completePlayers.filter { it.name.contains(filter) }.sort(sortedBy = newSorting)
                )
            }
        }
}