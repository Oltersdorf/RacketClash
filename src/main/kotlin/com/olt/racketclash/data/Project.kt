package com.olt.racketclash.data

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val name: String,
    val lastModified: String,
    val location: String,
    val playerNumber: Int = 0,
    val teamNumber: Int = 0
)