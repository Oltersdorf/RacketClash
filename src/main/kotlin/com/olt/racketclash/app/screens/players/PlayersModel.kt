package com.olt.racketclash.app.screens.players

import com.olt.racketclash.data.database.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.ProjectSettings
import com.olt.racketclash.data.sort
import com.olt.racketclash.state.ViewModelState

class PlayersModel(
    private val database: Database,
    private val projectId: Long
) : ViewModelState<PlayersModel.State>(initialState = State(projectId = projectId)) {

    private var completePlayers: List<Player> = emptyList()
    private var projectSettings: ProjectSettings? = null

    init {
        onIO {
            database.projectSettings(id = projectId).collect { settings ->
                projectSettings = settings

                if (settings != null) {
                    val players = completePlayers.toMutableList()
                    players.replaceAll {
                        it.copy(
                            wonGames = it.wonGames + (settings.gamePointsForBye * it.bye),
                            wonSets = it.wonSets + (settings.setPointsForBye * it.bye),
                            wonPoints = it.wonPoints + (settings.pointsForBye * it.bye)
                        )
                    }
                    completePlayers = players.toList()
                }
            }
        }

        onIO {
            database.players().collect { playerList ->
                updateState {
                    val players = playerList.toMutableList()

                    val settings = projectSettings
                    if (settings != null) {
                        players.replaceAll {
                            it.copy(
                                wonGames = it.wonGames + (settings.gamePointsForBye * it.bye),
                                wonSets = it.wonSets + (settings.setPointsForBye * it.bye),
                                wonPoints = it.wonPoints + (settings.pointsForBye * it.bye)
                            )
                        }
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
        val projectId: Long,
        val isLoading: Boolean = true,
        val players: List<Player> = emptyList(),
        val filter: String = "",
        val sortedBy: Player.Sorting = Player.Sorting.NameAscending
    )

    fun updateActive(id: Long, active: Boolean) =
        onIO {
            database.playerSetActive(id = id, active = active, projectId = projectId)
        }

    fun deletePlayer(id: Long) =
        onIO {
            database.deletePlayer(id = id, projectId = projectId)
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