package com.olt.racketclash.addorupdatetournament

data class Time(
    val hour: Int = 0,
    val minute: Int = 0
) {
    override fun toString(): String =
        "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}