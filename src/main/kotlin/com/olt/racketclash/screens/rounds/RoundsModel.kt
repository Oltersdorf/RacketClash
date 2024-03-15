package com.olt.racketclash.screens.rounds

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Player
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoundsModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<RoundsModel.Modal>(navigateToScreen, Modal()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.games().collect { gameList ->
                updateState { model ->
                    model.copy(games = gameList.groupBy { it.roundNumber }.toSortedMap())
                }
            }
        }
        screenModelScope.launch(context = Dispatchers.IO) {
            database.player().collect { playerList ->
                updateState { it.copy(player = playerList) }
            }
        }
    }

    data class Modal(
        val games: Map<Int, List<Game>> = emptyMap(),
        val player: List<Player> = emptyList()
    )

    fun addGame(roundName: String, playerLeft1Id: Long?, playerLeft2Id: Long?, playerRight1Id: Long?, playerRight2Id: Long?, isBye: Boolean) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.addGame(roundName = roundName, playerLeft1Id = playerLeft1Id, playerLeft2Id = playerLeft2Id, playerRight1Id = playerRight1Id, playerRight2Id = playerRight2Id, isBye = isBye)
        }
    }

    fun updateGame(id: Long, set1Left: Int, set1Right: Int, isDone: Boolean) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.updateGame(id = id, set1Left = set1Left, set1Right = set1Right, isDone = isDone)
        }
    }

    fun deleteRound(roundName: String) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.deleteRound(roundName = roundName)
        }
    }
}