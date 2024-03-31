package com.olt.racketclash.screens.editGame

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditGameModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database,
    private val roundId: Long
) : NavigableStateScreenModel<EditGameModel.Model>(navigateToScreen = navigateToScreen, initialState = Model()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.round(id = roundId).collect {
                updateState { copy(roundName = it?.name ?: "") }
            }
        }

        screenModelScope.launch(context = Dispatchers.IO) {
            database.player().collect {
                updateState { copy(players = it) }
            }
        }
    }

    data class Model(
        val roundName: String = "",
        val players: List<Player> = emptyList()
    )
}