package com.olt.racketclash.tournaments

data class Tournament(
    val id: Long,
    val name: String,
    val numberOfCourts: Int,
    val location: String,
    val startDateTime: String,
    val endDateTime: String,
    val playersCount: Int,
    val categoriesCount: Int
)