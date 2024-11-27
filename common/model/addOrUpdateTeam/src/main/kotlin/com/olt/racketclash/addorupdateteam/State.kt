package com.olt.racketclash.addorupdateteam

import com.olt.racketclash.database.team.DeletableTeam

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val team: DeletableTeam = DeletableTeam(
        id = 0L,
        name = "",
        size = 0,
        winRatioSingle = Triple(0, 0, 0),
        winRatioDouble = Triple(0, 0, 0),
        deletable = false
    )
)