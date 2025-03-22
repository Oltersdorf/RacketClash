package com.olt.racketclash.state.rule

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.Filter
import com.olt.racketclash.database.table.Rule
import com.olt.racketclash.state.ViewModelState

class RulesModel(
    private val database: Database
) : ViewModelState<RulesState>(initialState = RulesState()) {

    val rules by lazy { RuleListModel(database = database) { state.value.filter } }

    fun applyFilter(filter: Filter) {
        updateState { copy(filter = filter) }
        rules.updateList(filter = filter)
    }

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
    ) {
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
            rules.updateList(filter = state.value.filter)
        }
    }
}