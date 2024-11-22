package com.olt.racketclash.player

data class Game(
    val id: Long,
    val date: String,
    val playerLeftOneId: Long,
    val playerLeftOneName: String,
    val playerLeftTwoId: Long?,
    val playerLeftTwoName: String?,
    val playerRightOneId: Long,
    val playerRightOneName: String,
    val playerRightTwoId: Long?,
    val playerRightTwoName: String?,
    val results: List<Pair<Int, Int>>,
    val tournamentId: Long,
    val tournamentName: String,
    val categoryId: Long,
    val categoryName: String,
    val ruleId: Long,
    val ruleName: String,
    val totalGamePoints: Pair<Int, Int>,
    val totalSetPoints: Pair<Int, Int>,
    val totalPointPoints: Pair<Int, Int>
)