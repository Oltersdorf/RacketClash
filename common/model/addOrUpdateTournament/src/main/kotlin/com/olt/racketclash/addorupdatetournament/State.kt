package com.olt.racketclash.addorupdatetournament

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val name: String = "",
    val location: String = "",
    val suggestedLocations: List<String> = emptyList(),
    val courts: Int = 1,
    val dateRangeStart: Long? = null,
    val dateRangeEnd: Long? = null,
    val timeStart: Time = Time(),
    val timeEnd: Time = Time(),
    val suggestedTimes: List<Time> = emptyList()
)