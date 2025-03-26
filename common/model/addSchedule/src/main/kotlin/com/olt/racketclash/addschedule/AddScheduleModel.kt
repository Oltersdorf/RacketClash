package com.olt.racketclash.addschedule

import com.olt.racketclash.database.api.Schedule
import com.olt.racketclash.database.api.ScheduleDatabase
import com.olt.racketclash.state.ViewModelState
import java.time.Instant

class AddScheduleModel(
    private val database: ScheduleDatabase,
    private val categoryId: Long,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    private val doubleGenerate = EquallyStrongDoublesGenerator()

    fun updateRounds(number: Int) =
        updateState { copy(rounds = number) }

    fun updateDifferentPartnersEachRound(value: Boolean) =
        updateState { copy(differentPartnersEachRound = value) }

    fun updateOnlyOneRestPerPlayer(value: Boolean) =
        updateState { copy(onlyOneRestPerPlayer = value) }

    fun updateWorstStrengthDifferenceIsZero(value: Boolean) =
        updateState { copy(worstStrengthDifferenceIsZero = value) }

    fun updateMaxRepeats(number: Int) =
        updateState { copy(maxRepeats = number) }

    fun updateActive(playerId: Long, checked: Boolean) =
        updateState {
            if (checked)
                copy(selectedPlayers = selectedPlayers + playerId)
            else
                copy(selectedPlayers = selectedPlayers - playerId)
        }

    fun updatePage(pageNumber: Int) =
        onIO {  }

    fun generate() =
        onDefault {
            updateState { copy(isGenerating = true, isSavable = false) }

            updateState {
                val (games, rests, worstPerformance) = doubleGenerate.getDoubles(
                    rounds = rounds,
                    players = players.map { Player(id = it.id, name = it.name, teamStrength = 1) },
                    differentPartnersEachRound = differentPartnersEachRound,
                    tryUntilWorstPerformanceIsZero = worstStrengthDifferenceIsZero,
                    tryUntilNoMoreThanOneRest = onlyOneRestPerPlayer,
                    maxRepeat = maxRepeats
                )

                copy(
                    isSavable = true,
                    isGenerating = false,
                    generatedGames = games.values.flatten(),
                    generatedRests = rests.values.flatten(),
                    generatedPerformance = worstPerformance
                )
            }
        }

    fun onSave(callback: () -> Unit) =
        onIO {
            updateState { copy(isSavable = false) }

            val scheduledGames = state.value.generatedGames

            scheduledGames.forEachIndexed { index, scheduledGame ->
                val schedule = Schedule(
                    categoryId = categoryId,
                    categoryOrderNumber = index,
                    tournamentId = tournamentId,
                    playerIdLeftOne = scheduledGame.player1LeftId,
                    playerIdLeftTwo = scheduledGame.player2LeftId,
                    playerIdRightOne = scheduledGame.player1RightId,
                    playerIdRightTwo = scheduledGame.player2RightId,
                    scheduledFor = Instant.EPOCH
                )
                database.add(schedule = schedule)
            }

            updateState { copy(isSavable = true) }

            callback()
        }
}