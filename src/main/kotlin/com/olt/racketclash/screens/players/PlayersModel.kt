package com.olt.racketclash.screens.players

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayersModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<PlayersModel.Modal>(navigateToScreen, Modal()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.player().collect {
                updateState { copy(isLoading = false, players = it) }
            }
        }
    }

    data class Modal(
        val isLoading: Boolean = true,
        val players: List<Player> = emptyList()
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
}