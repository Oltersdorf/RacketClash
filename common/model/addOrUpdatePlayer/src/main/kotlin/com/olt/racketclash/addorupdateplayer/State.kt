package com.olt.racketclash.addorupdateplayer

import com.olt.racketclash.database.player.DeletablePlayer

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val player: DeletablePlayer = DeletablePlayer(
        id = 0L,
        name = "",
        birthYear = 1900,
        club = "",
        numberOfTournaments = 0,
        goldMedals = 0,
        silverMedals = 0,
        bronzeMedals = 0,
        winRatioSingle = Triple(0, 0, 0),
        winRatioDouble = Triple(0, 0, 0),
        deletable = false
    ),
    val clubSuggestions: List<String> = emptyList()
)