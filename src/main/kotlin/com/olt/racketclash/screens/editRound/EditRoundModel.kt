package com.olt.racketclash.screens.editRound

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Round
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRoundModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database,
    private val round: Round
) : NavigableStateScreenModel<EditRoundModel.Model>(navigateToScreen = navigateToScreen, initialState = Model()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.games().collect { gamesList ->
                updateState {
                    Model(
                        games = gamesList.filter { it.roundId == round.id }
                    )
                }
            }
        }
    }

    data class Model(
        val games: List<Game> = emptyList()
    )
}