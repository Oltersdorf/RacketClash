package com.olt.racketclash.screens.players

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.database.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.sort
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayersModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<PlayersModel.Modal>(navigateToScreen, Modal()) {

    private var completePlayers: List<Player> = emptyList()

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.players().collect {
                updateState {
                    completePlayers = it
                    copy(
                        isLoading = false,
                        players = it.filter { it.name.contains(filter) }.sort(sortedBy = sortedBy)
                    )
                }
            }
        }
    }

    data class Modal(
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
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    filter = newFilter,
                    players = completePlayers.filter { it.name.contains(newFilter) }.sort(sortedBy = sortedBy)
                )
            }
        }
    }

    fun changeSorting(newSorting: Player.Sorting) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    sortedBy = newSorting,
                    players = completePlayers.filter { it.name.contains(filter) }.sort(sortedBy = newSorting)
                )
            }
        }
    }
}