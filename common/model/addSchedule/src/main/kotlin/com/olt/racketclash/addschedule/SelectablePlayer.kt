package com.olt.racketclash.addschedule

data class SelectablePlayer(
    val id: Long,
    val selected: Boolean,
    val name: String,
    val team: String
)