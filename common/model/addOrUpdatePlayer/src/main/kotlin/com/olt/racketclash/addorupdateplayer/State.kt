package com.olt.racketclash.addorupdateplayer

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val name: String = "",
    val birthYear: Int = 1900,
    val club: String = "",
    val clubSuggestions: List<String> = emptyList()
)