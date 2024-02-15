package com.olt.racketclash.data

data class Game(
    val id: Long,
    val roundName: String,
    val roundNumber: Int,
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
    val set1Right: Int
)
