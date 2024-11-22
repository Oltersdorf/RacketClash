package com.olt.racketclash.teams

data class Team(
    val id: Long,
    val name: String,
    val size: Int,
    val winRatioSingle: Triple<Int, Int, Int>,
    val winRatioDouble: Triple<Int, Int, Int>
)