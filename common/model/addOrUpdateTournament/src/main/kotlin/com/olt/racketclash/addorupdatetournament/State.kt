package com.olt.racketclash.addorupdatetournament

import com.olt.racketclash.database.api.Tournament

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val tournament: Tournament = Tournament(),
    val suggestedLocations: List<String> = emptyList(),
    val suggestedTimes: List<String> = emptyList(),
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val timeStart: String = "00:00",
    val timeEnd: String = "00:00"
)