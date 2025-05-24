package com.olt.racketclash.state.club

import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.Tournament

data class ClubData(
    val players: List<Player> = emptyList(),
    val tournaments: List<Tournament> = emptyList()
)