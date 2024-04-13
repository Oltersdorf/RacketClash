package com.olt.racketclash.data

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val name: String,
    val lastModified: String,
    val location: String,
    val playerNumber: Int = 0,
    val teamNumber: Int = 0,
    val fields: Int = 1,
    val timeout: Int = 1,
    val gamePointsForBye: Int = 0,
    val setPointsForBye: Int = 0,
    val pointsForBye: Int = 0
)