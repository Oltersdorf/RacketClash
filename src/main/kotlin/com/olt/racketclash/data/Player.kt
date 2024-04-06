package com.olt.racketclash.data

data class Player(
    val id: Long = -1,
    val active: Boolean = false,
    val name: String = "",
    val teamId: Long = -1,
    val teamName: String = "",
    val teamStrength: Int = 0,
    val openGames: Int = 0,
    val played: Int = 0,
    val bye: Int = 0,
    val wonGames: Int = 0,
    val lostGames: Int = 0,
    val wonSets: Int = 0,
    val lostSets: Int = 0,
    val wonPoints: Int = 0,
    val lostPoints: Int = 0
)