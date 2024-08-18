package com.olt.racketclash.app.screens.editRound

import com.olt.racketclash.data.Bye
import com.olt.racketclash.data.database.Database
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Round
import com.olt.racketclash.state.ViewModelState

class EditRoundModel(
    private val database: Database,
    private val roundId: Long,
    private val projectId: Long
) : ViewModelState<EditRoundModel.State>(initialState = State(projectId = projectId)) {

    init {
        onIO {
            database.games(roundId = roundId).collect {
                updateState { copy(games = it) }
            }
        }

        onIO {
            database.round(id = roundId).collect {
                updateState { copy(round = it, temporaryRoundName = it?.name ?: "") }
            }
        }

        onIO {
            database.bye(roundId = roundId).collect {
                updateState { copy(byes = it) }
            }
        }
    }

    data class State(
        val projectId: Long,
        val round: Round? = null,
        val temporaryRoundName: String = "",
        val games: List<Game> = emptyList(),
        val byes: List<Bye> = emptyList()
    )

    fun updateTemporaryRoundName(newRoundName: String) =
        onDefault {
            updateState { copy(temporaryRoundName = newRoundName) }
        }

    fun saveRoundName() =
        onIO {
            database.updateRoundName(id = roundId, name = state.value.temporaryRoundName, projectId = projectId)
        }

    fun deleteGame(gameId: Long) =
        onIO {
            database.deleteGame(id = gameId, projectId = projectId)
        }

    fun deleteBye(byeId: Long) =
        onIO {
            database.deleteBye(id = byeId, projectId = projectId)
        }
}