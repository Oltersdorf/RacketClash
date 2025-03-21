package com.olt.rackeclash.rules

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.Sorting
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.database.table.Rule
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class RulesModel(
    private val database: Database,
    private val pageSize: Int
) : ViewModelState<State>(initialState = State()) {

    init { updateRulesState() }

    fun search(name: String) =
        updateRulesState(currentState = state.value.copy(nameSearch = name))

    fun onSort(sorting: Sorting) =
        updateRulesState(currentState = state.value.copy(sorting = sorting))

    fun updatePage(pageNumber: Int) =
        updateRulesState(currentPage = pageNumber)

    fun addRule(
        name: String,
        maxSets: Int,
        winSets: Int,
        maxPoints: Int,
        winPoints: Int,
        pointsDifference: Int,
        gamePointsForWin: Int,
        gamePointsForLose: Int,
        gamePointsForDraw: Int,
        gamePointsForRest: Int,
        setPointsForRest: Int,
        pointPointsForRest: Int
    ) =
        onIO {
            database.rules.add(
                Rule(
                    id = -1,
                    name = name,
                    maxSets = maxSets,
                    winSets = winSets,
                    maxPoints = maxPoints,
                    winPoints = winPoints,
                    pointsDifference = pointsDifference,
                    gamePointsForWin = gamePointsForWin,
                    gamePointsForLose = gamePointsForLose,
                    gamePointsForDraw = gamePointsForDraw,
                    gamePointsForRest = gamePointsForRest,
                    setPointsForRest = setPointsForRest,
                    pointPointsForRest = pointPointsForRest,
                    used = 0
                )
            )
            updateRulesState()
        }

    fun deleteRule(rule: FilteredAndOrderedRule) =
        onIO {
            updateState { copy(rules = rules - rule) }
            database.rules.delete(id = rule.id)
        }

    private fun updateRulesState(
        currentState: State = state.value,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { currentState.copy(isLoading = true) }

            val (totalSize, sortedRules) = database.rules.selectFilteredAndOrdered(
                nameFilter = currentState.nameSearch,
                sorting = currentState.sorting,
                fromIndex = (currentPage - 1) * pageSize,
                toIndex = currentPage * pageSize
            )

            updateState {
                copy(
                    isLoading = false,
                    rules = sortedRules,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
    }