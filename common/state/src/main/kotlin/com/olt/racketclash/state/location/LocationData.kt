package com.olt.racketclash.state.location

import com.olt.racketclash.database.api.Tournament

data class LocationData(
    val tournaments: List<Tournament> = emptyList()
)