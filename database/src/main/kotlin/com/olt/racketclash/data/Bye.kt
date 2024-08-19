package com.olt.racketclash.data

data class Bye(
    val id: Long = -1,
    val roundId: Long = -1,
    val playerId: Long = -1,
    val playerName: String? = null,
    val playerTeamName: String? = null
)