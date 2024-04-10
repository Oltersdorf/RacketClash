package com.olt.racketclash.screens.editRound

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Bye
import com.olt.racketclash.database.Database
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
            database.games(roundId = round.id).collect {
                updateState { copy(games = it) }
            }
        }

        screenModelScope.launch(context = Dispatchers.IO) {
            database.round(id = round.id).collect {
                updateState { copy(round = it, temporaryRoundName = it?.name ?: "") }
            }
        }

        screenModelScope.launch(context = Dispatchers.IO) {
            database.bye(roundId = round.id).collect {
                updateState { copy(byes = it) }
            }
        }
    }

    data class Model(
        val round: Round? = null,
        val temporaryRoundName: String = "",
        val games: List<Game> = emptyList(),
        val byes: List<Bye> = emptyList()
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

    fun deleteGame(gameId: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.deleteGame(id = gameId)
        }
    }

    fun deleteBye(byeId: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.deleteBye(id = byeId)
        }
    }
}