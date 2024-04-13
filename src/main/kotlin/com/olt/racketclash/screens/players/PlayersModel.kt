package com.olt.racketclash.screens.players

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.database.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.sort
import com.olt.racketclash.language.translations.Language
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayersModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database,
    private val fileHandler: FileHandler,
    language: Language
) : NavigableStateScreenModel<PlayersModel.Modal>(navigateToScreen, Modal(language = language)) {

    private var completePlayers: List<Player> = emptyList()

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.players().collect { playerList ->
                updateState {
                    val players = playerList.toMutableList()
                    val project = fileHandler.currentProject

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

    data class Modal(
        val language: Language,
        val isLoading: Boolean = true,
        val players: List<Player> = emptyList(),
        val filter: String = "",
        val sortedBy: Player.Sorting = Player.Sorting.NameAscending
    )

    fun updateActive(id: Long, active: Boolean) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.playerSetActive(id = id, active = active)
        }
    }

    fun deletePlayer(id: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.deletePlayer(id = id)
        }
    }

    fun changeFilter(newFilter: String) {
        screenModelScope.launch(context = Dispatchers.IO) {
            updateState {
                copy(
                    filter = newFilter,
                    players = completePlayers.filter { it.name.contains(newFilter) }.sort(sortedBy = sortedBy)
                )
            }
        }
    }

    fun changeSorting(newSorting: Player.Sorting) {
        screenModelScope.launch(context = Dispatchers.IO) {
            updateState {
                copy(
                    sortedBy = newSorting,
                    players = completePlayers.filter { it.name.contains(filter) }.sort(sortedBy = newSorting)
                )
            }
        }
    }
}