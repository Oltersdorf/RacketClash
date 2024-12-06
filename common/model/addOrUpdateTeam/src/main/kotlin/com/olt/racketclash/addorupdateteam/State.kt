package com.olt.racketclash.addorupdateteam

import com.olt.racketclash.database.table.Team
import com.olt.racketclash.database.team.emptyTeam

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val team: Team = emptyTeam()
)