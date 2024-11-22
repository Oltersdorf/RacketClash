package com.olt.rackeclash.rules

data class Rule(
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
    val pointPointsForRest: Int
)
