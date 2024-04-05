package com.olt.racketclash.data

data class Game(
    val id: Long = -1,
    val roundId: Long = -1,
    val isDone: Boolean = false,
    val isBye: Boolean = false,
    val playerLeft1Id: Long? = null,
    val playerLeft1Name: String? = null,
    val playerLeft2Id: Long? = null,
    val playerLeft2Name: String? = null,
    val playerRight1Id: Long? = null,
    val playerRight1Name: String? = null,
    val playerRight2Id: Long? = null,
    val playerRight2Name: String? = null,
    val set1Left: Int = 0,
    val set1Right: Int = 0,
    val set2Left: Int = 0,
    val set2Right: Int = 0,
    val set3Left: Int = 0,
    val set3Right: Int = 0,
    val set4Left: Int = 0,
    val set4Right: Int = 0,
    val set5Left: Int = 0,
    val set5Right: Int = 0
)
