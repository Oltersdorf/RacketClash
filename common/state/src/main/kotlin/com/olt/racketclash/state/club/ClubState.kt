package com.olt.racketclash.state.club

import com.olt.racketclash.database.api.Club
import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.Tournament

data class ClubState(
    val isLoading: Boolean = true,
    val club: Club = Club(),
    val players: List<Player> = emptyList(),
    val tournaments: List<Tournament> = emptyList()
)