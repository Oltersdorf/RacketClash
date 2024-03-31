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
            database.games().collect {
                updateState {
                    copy(games = it.filter { it.roundId == it.id })
                }
            }
        }

        screenModelScope.launch(context = Dispatchers.IO) {
            database.round(id = round.id).collect {
                updateState { copy(round = it) }
            }
        }
    }

    data class Model(
        val round: Round? = null,
        val temporaryRoundName: String = round?.name ?: "",
        val games: List<Game> = emptyList()
    )

    fun updateTemporaryRoundName(newRoundName: String) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState { copy(temporaryRoundName = newRoundName) }
        }
    }

    fun saveRoundName() {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.updateRoundName(id = round.id, name = state.value.temporaryRoundName)
        }
    }
}