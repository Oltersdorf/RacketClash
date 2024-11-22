package com.olt.rackeclash.addorupdaterule

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val name: String = "",
    val maxSets: Int = 3,
    val winSets: Int = 2,
    val maxPoints: Int = 30,
    val winPoints: Int = 21,
    val pointsDifference: Int = 2,
    val gamePointsForWin: Int = 2,
    val gamePointsForLose: Int = 0,
    val gamePointsForDraw: Int = 1,
    val gamePointsForRest: Int = 2,
    val setPointsForRest: Int = 0,
    val pointPointsForRest: Int = 0
)