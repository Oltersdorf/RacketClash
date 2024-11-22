package com.olt.rackeclash.addorupdaterule

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.ViewModelState
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class AddOrUpdateRuleModel(
    private val database: Database,
    private val ruleId: Long?
) : ViewModelState<State>(initialState = State()) {

    fun updateName(newName: String) =
        updateState { copy(isSavable = newName.isNotBlank(), name = newName) }

    fun maxSetsUp(newNumber: Int) =
        updateState {
            copy(
                maxSets = newNumber,
                winSets = max(winSets, ceil(maxSets.toDouble() / 2).roundToInt())
            )
        }

    fun maxSetsDown(newNumber: Int) =
        updateState {
            copy(
                maxSets = newNumber,
                winSets = min(maxSets, winSets)
            )
        }

    fun winSetsUp(newNumber: Int) =
        updateState {
            copy(
                winSets = newNumber,
                maxSets = max(maxSets, winSets)
            )
        }

    fun winSetsDown(newNumber: Int) =
        updateState {
            copy(
                winSets = newNumber,
                maxSets = min(maxSets, winSets * 2)
            )
        }

    fun maxPointsUp(newNumber: Int) =
        updateState { copy(maxPoints = newNumber) }

    fun maxPointsDown(newNumber: Int) =
        updateState {
            copy(
                maxPoints = newNumber,
                winPoints = min(winPoints, maxPoints),
                pointsDifference = min(pointsDifference, winPoints)
            )
        }

    fun winPointsUp(newNumber: Int) =
        updateState {
            copy(
                winPoints = newNumber,
                maxPoints = max(maxPoints, winPoints)
            )
        }

    fun winPointsDown(newNumber: Int) =
        updateState {
            copy(
                winPoints = newNumber,
                pointsDifference = min(pointsDifference, winPoints)
            )
        }

    fun pointsDifferenceUp(newNumber: Int) =
        updateState {
            copy(
                pointsDifference = newNumber,
                winPoints = max(winPoints, pointsDifference),
                maxPoints = max(maxPoints, winPoints)
            )
        }

    fun pointsDifferenceDown(newNumber: Int) =
        updateState { copy(pointsDifference = newNumber) }

    fun updateGamePointsForWin(newNumber: Int) =
        updateState { copy(gamePointsForWin = newNumber) }

    fun updateGamePointsForLose(newNumber: Int) =
        updateState { copy(gamePointsForLose = newNumber) }

    fun updateGamePointsForDraw(newNumber: Int) =
        updateState { copy(gamePointsForDraw = newNumber) }

    fun updateGamePointsForRest(newNumber: Int) =
        updateState { copy(gamePointsForRest = newNumber) }

    fun updateSetPointsForRest(newNumber: Int) =
        updateState { copy(setPointsForRest = newNumber) }

    fun updatePointPointsForRest(newNumber: Int) =
        updateState { copy(pointPointsForRest = newNumber) }

    fun save() =
        onIO {
            //write to database
        }
}