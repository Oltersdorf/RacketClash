package com.olt.racketclash.schedule

data class ScheduledGame(
    val id: Long,
    val active: Boolean,
    val scheduled: String,
    val single: Boolean,
    val sets: List<Pair<String, String>>,
    val categoryName: String,
    val playerLeftOneName: String,
    val playerLeftTwoName: String,
    val playerRightOneName: String,
    val playerRightTwoName: String
)