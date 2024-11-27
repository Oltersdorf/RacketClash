package com.olt.racketclash.addorupdatetournament

import com.olt.racketclash.database.tournament.DeletableTournament

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val tournament: DeletableTournament = DeletableTournament(
        id = 0L,
        name = "",
        numberOfCourts = 1,
        location = "",
        startDateTime = "",
        endDateTime = "",
        playersCount = 0,
        categoriesCount = 0,
        deletable = false
    ),
    val suggestedLocations: List<String> = emptyList(),
    val suggestedTimes: List<String> = emptyList(),
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val timeStart: String = "00:00",
    val timeEnd: String = "00:00"
)