package com.olt.racketclash.screens.games

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Round
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GamesModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<GamesModel.Model>(navigateToScreen, Model()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.rounds().collect { roundList ->
                updateState { model ->
                    Model(rounds = roundList.associateWith { model.rounds[it] ?: emptyList() })
                }
            }
        }
        screenModelScope.launch(context = Dispatchers.IO) {
            database.games().collect { gamesList ->
                updateState { model ->
                    val groupedGames = gamesList.groupBy { it.roundId }
                    Model(rounds = model.rounds.mapValues { groupedGames[it.key.id] ?: emptyList() })
                }
            }
        }
    }

    data class Model(
        val rounds: Map<Round, List<Game>> = emptyMap()
    )

    fun updateGame(
        id: Long,
        set1Left: Int,
        set1Right: Int,
        set2Left: Int,
        set2Right: Int,
        set3Left: Int,
        set3Right: Int,
        set4Left: Int,
        set4Right: Int,
        set5Left: Int,
        set5Right: Int,
        isDone: Boolean) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.updateGame(
                id = id,
                set1Left = set1Left,
                set1Right = set1Right,
                set2Left = set2Left,
                set2Right = set2Right,
                set3Left = set3Left,
                set3Right = set3Right,
                set4Left = set4Left,
                set4Right = set4Right,
                set5Left = set5Left,
                set5Right = set5Right,
                isDone = isDone
            )
        }
    }
}