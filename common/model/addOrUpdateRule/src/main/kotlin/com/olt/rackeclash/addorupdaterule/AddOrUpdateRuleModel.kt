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

    init {
        onIO {
            if (ruleId == null)
                updateState { copy(isLoading = false) }
            else {
                updateState {
                    copy(
                        isSavable = true,
                        isLoading = false,
                        rule = database.rules.selectSingle(id = ruleId)
                    )
                }
            }
        }
    }

    fun updateName(newName: String) =
        updateState { copy(isSavable = newName.isNotBlank(), rule = rule.copy(name = newName)) }

    fun maxSetsUp(newNumber: Int) =
        updateState {
            copy(
                rule = rule.copy(
                    maxSets = newNumber,
                    winSets = max(rule.winSets, ceil(rule.maxSets.toDouble() / 2).roundToInt())
                )
            )
        }

    fun maxSetsDown(newNumber: Int) =
        updateState {
            copy(
                rule = rule.copy(
                    maxSets = newNumber,
                    winSets = min(rule.maxSets, rule.winSets)
                )
            )
        }

    fun winSetsUp(newNumber: Int) =
        updateState {
            copy(
                rule = rule.copy(
                    winSets = newNumber,
                    maxSets = max(rule.maxSets, rule.winSets)
                )
            )
        }

    fun winSetsDown(newNumber: Int) =
        updateState {
            copy(
                rule = rule.copy(
                    winSets = newNumber,
                    maxSets = min(rule.maxSets, rule.winSets * 2)
                )
            )
        }

    fun maxPointsUp(newNumber: Int) =
        updateState { copy(rule = rule.copy(maxPoints = newNumber)) }

    fun maxPointsDown(newNumber: Int) =
        updateState {
            copy(
                rule = rule.copy(
                    maxPoints = newNumber,
                    winPoints = min(rule.winPoints, rule.maxPoints),
                    pointsDifference = min(rule.pointsDifference, rule.winPoints)
                )
            )
        }

    fun winPointsUp(newNumber: Int) =
        updateState {
            copy(
                rule = rule.copy(
                    winPoints = newNumber,
                    maxPoints = max(rule.maxPoints, rule.winPoints)
                )
            )
        }

    fun winPointsDown(newNumber: Int) =
        updateState {
            copy(
                rule = rule.copy(
                    winPoints = newNumber,
                    pointsDifference = min(rule.pointsDifference, rule.winPoints)
                )
            )
        }

    fun pointsDifferenceUp(newNumber: Int) =
        updateState {
            copy(
                rule = rule.copy(
                    pointsDifference = newNumber,
                    winPoints = max(rule.winPoints, rule.pointsDifference),
                    maxPoints = max(rule.maxPoints, rule.winPoints)
                )
            )
        }

    fun pointsDifferenceDown(newNumber: Int) =
        updateState { copy(rule = rule.copy(pointsDifference = newNumber)) }

    fun updateGamePointsForWin(newNumber: Int) =
        updateState { copy(rule = rule.copy(gamePointsForWin = newNumber)) }

    fun updateGamePointsForLose(newNumber: Int) =
        updateState { copy(rule = rule.copy(gamePointsForLose = newNumber)) }

    fun updateGamePointsForDraw(newNumber: Int) =
        updateState { copy(rule = rule.copy(gamePointsForDraw = newNumber)) }

    fun updateGamePointsForRest(newNumber: Int) =
        updateState { copy(rule = rule.copy(gamePointsForRest = newNumber)) }

    fun updateSetPointsForRest(newNumber: Int) =
        updateState { copy(rule = rule.copy(setPointsForRest = newNumber)) }

    fun updatePointPointsForRest(newNumber: Int) =
        updateState { copy(rule = rule.copy(pointPointsForRest = newNumber)) }

    fun save() =
        onIO {
            if (ruleId == null)
                database.rules.add(rule = state.value.rule)
            else
                database.rules.update(rule = state.value.rule)
        }
}