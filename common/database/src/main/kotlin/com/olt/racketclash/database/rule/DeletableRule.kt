package com.olt.racketclash.database.rule

data class DeletableRule(
    val id: Long,
    val name: String,
    val maxSets: Int,
    val winSets: Int,
    val maxPoints: Int,
    val winPoints: Int,
    val pointsDifference: Int,
    val gamePointsForWin: Int,
    val gamePointsForLose: Int,
    val gamePointsForDraw: Int,
    val gamePointsForRest: Int,
    val setPointsForRest: Int,
    val pointPointsForRest: Int,
    val deletable: Boolean
)

internal fun SelectFilteredAndOrdered.toDeletableRule(): DeletableRule =
    DeletableRule(
        id = id,
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
        deletable = deletable != 0L
    )

internal fun SelectSingle.toDeletableRule(): DeletableRule =
    DeletableRule(
        id = id,
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
        deletable = deletable != 0L
    )