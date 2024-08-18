package com.olt.racketclash.data

data class ProjectSettings(
    val id: Long,
    val fields: Int,
    val timeout: Int,
    val gamePointsForBye: Int,
    val setPointsForBye: Int,
    val pointsForBye: Int
)
