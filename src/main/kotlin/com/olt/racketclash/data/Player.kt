package com.olt.racketclash.data

data class Player(
    val id: Long = -1,
    val active: Boolean = false,
    val name: String = "",
    val teamId: Long = -1,
    val teamName: String = "",
    val teamStrength: Int = 0,
    val played: Int = 0,
    val bye: Int = 0,
    val games: Pair<Int, Int> = Pair(0, 0),
    val sets: Pair<Int, Int> = Pair(0, 0),
    val points: Pair<Int, Int> = Pair(0, 0)
)