package com.olt.racketclash.team

data class Player(
    val id: Long,
    val name: String,
    val birthYear: Int,
    val club: String,
    val winRatioSingle: Triple<Int, Int, Int>,
    val winRatioDouble: Triple<Int, Int, Int>
)