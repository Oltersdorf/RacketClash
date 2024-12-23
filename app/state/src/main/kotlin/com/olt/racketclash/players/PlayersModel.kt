package com.olt.racketclash.players

import com.olt.racketclash.data.Player
import com.olt.racketclash.data.ProjectSettings
import com.olt.racketclash.data.database.IPlayerDatabase
import com.olt.racketclash.data.database.IProjectDatabase
import com.olt.racketclash.data.sort
import com.olt.racketclash.state.ViewModelState

class PlayersModel(
    private val projectDatabase: IProjectDatabase,
    private val playerDatabase: IPlayerDatabase,
    private val projectId: Long
) : ViewModelState<PlayersModel.State>(initialState = State(projectId = projectId)) {

    private var completePlayers: List<Player> = emptyList()
    private var projectSettings: ProjectSettings? = null

    init {
        onIO {
            projectDatabase.projectSettings(id = projectId).collect { settings ->
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
            playerDatabase.players().collect { playerList ->
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
            playerDatabase.setActive(id = id, active = active, projectId = projectId)
        }

    fun deletePlayer(id: Long) =
        onIO {
            playerDatabase.delete(id = id, projectId = projectId)
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