package com.olt.racketclash.players

data class Player(
    val id: Long,
    val name: String,
    val birthYear: Int,
    val club: String,
    val numberOfTournaments: Int,
    val goldMedals: Int,
    val silverMedals: Int,
    val bronzeMedals: Int,
    val winRatioSingle: Triple<Int, Int, Int>,
    val winRatioDouble: Triple<Int, Int, Int>
)