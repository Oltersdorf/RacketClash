package com.olt.racketclash.screens.players

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.database.Database
import com.olt.racketclash.data.Player
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
                    copy(isLoading = false, players = it.sortAndFilter(filter = filter, sortedBy = sortedBy))
                }
            }
        }
    }

    sealed class Sorting {
        data object NameAscending : Sorting()
        data object NameDescending : Sorting()
        data object TeamAscending : Sorting()
        data object TeamDescending : Sorting()
        data object PointsAscending : Sorting()
        data object PointsDescending : Sorting()
        data object PendingAscending : Sorting()
        data object PendingDescending : Sorting()
        data object ByeAscending : Sorting()
        data object ByeDescending : Sorting()
        data object PlayedAscending : Sorting()
        data object PlayedDescending : Sorting()
    }

    data class Modal(
        val isLoading: Boolean = true,
        val players: List<Player> = emptyList(),
        val filter: String = "",
        val availableSorting: List<Sorting> =
            listOf(
                Sorting.NameAscending, Sorting.NameDescending,
                Sorting.TeamAscending, Sorting.TeamDescending,
                Sorting.PointsAscending, Sorting.PointsDescending,
                Sorting.PendingAscending, Sorting.PendingDescending,
                Sorting.PlayedAscending, Sorting.PlayedDescending,
                Sorting.ByeAscending, Sorting.ByeDescending
            ),
        val sortedBy: Sorting = Sorting.NameAscending
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
                    players = completePlayers.sortAndFilter(filter = newFilter, sortedBy = sortedBy)
                )
            }
        }
    }

    fun changeSorting(newSorting: Sorting) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    sortedBy = newSorting,
                    players = completePlayers.sortAndFilter(filter = filter, sortedBy = newSorting)
                )
            }
        }
    }

    private fun List<Player>.sortAndFilter(filter: String, sortedBy: Sorting): List<Player> {
        val teams = filter { it.name.contains(filter) }

        return when (sortedBy) {
            Sorting.NameAscending -> teams.sortedBy { it.name }
            Sorting.NameDescending -> teams.sortedByDescending { it.name }
            Sorting.PointsAscending -> teams.sortedWith(compareBy(Player::wonGames, Player::lostGames, Player::wonSets, Player::lostSets, Player::wonPoints, Player::lostPoints))
            Sorting.PointsDescending -> teams.sortedWith(compareBy(Player::wonGames, Player::lostGames, Player::wonSets, Player::lostSets, Player::wonPoints, Player::lostPoints).reversed())
            Sorting.TeamAscending -> teams.sortedBy { it.teamName }
            Sorting.TeamDescending -> teams.sortedByDescending { it.teamName }
            Sorting.ByeAscending -> teams.sortedBy { it.bye }
            Sorting.ByeDescending -> teams.sortedByDescending { it.bye }
            Sorting.PendingAscending -> teams.sortedBy { it.openGames }
            Sorting.PendingDescending -> teams.sortedByDescending { it.openGames }
            Sorting.PlayedAscending -> teams.sortedBy { it.played }
            Sorting.PlayedDescending -> teams.sortedByDescending { it.played }
        }
    }
}