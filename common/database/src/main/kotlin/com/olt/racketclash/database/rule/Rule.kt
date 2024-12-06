package com.olt.racketclash.database.rule

import com.olt.racketclash.database.table.Rule

fun emptyRule() =
    Rule(
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
        used = 0L
    )