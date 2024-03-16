package com.olt.racketclash.data

data class Game(
    val id: Long,
    val roundId: Long,
    val isDone: Boolean,
    val isBye: Boolean,
    val playerLeft1Id: Long?,
    val playerLeft1Name: String?,
    val playerLeft2Id: Long?,
    val playerLeft2Name: String?,
    val playerRight1Id: Long?,
    val playerRight1Name: String?,
    val playerRight2Id: Long?,
    val playerRight2Name: String?,
    val set1Left: Int,
    val set1Right: Int,
    val set2Left: Int,
    val set2Right: Int,
    val set3Left: Int,
    val set3Right: Int,
    val set4Left: Int,
    val set4Right: Int,
    val set5Left: Int,
    val set5Right: Int
)
