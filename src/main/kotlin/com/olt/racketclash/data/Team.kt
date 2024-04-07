package com.olt.racketclash.data

data class Team(
    val id: Long,
    val name: String,
    val strength: Int,
    val size: Int,
    val openGames: Int = 0,
    val bye: Int = 0,
    val played: Int = 0,
    val wonGames: Int = 0,
    val lostGames: Int = 0,
    val wonSets: Int = 0,
    val lostSets: Int = 0,
    val wonPoints: Int = 0,
    val lostPoints: Int = 0
)
