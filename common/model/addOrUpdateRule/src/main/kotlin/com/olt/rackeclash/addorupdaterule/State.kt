package com.olt.rackeclash.addorupdaterule

import com.olt.racketclash.database.rule.DeletableRule

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val rule: DeletableRule = DeletableRule(
        id = 0L,
        name = "",
        maxSets = 3,
        winSets = 2,
        maxPoints = 30,
        winPoints = 21,
        pointsDifference = 2,
        gamePointsForWin = 2,
        gamePointsForLose = 0,
        gamePointsForDraw = 1,
        gamePointsForRest = 2,
        setPointsForRest = 0,
        pointPointsForRest = 0,
        deletable = false
    )
)