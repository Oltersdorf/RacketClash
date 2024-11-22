package com.olt.racketclash.player

data class State(
    val isLoading: Boolean = true,
    val birthYear: Int = 1900,
    val club: String = "",
    val firstGame: String = "",
    val lastGame: String = "",
    val doubleGamePoints: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val doubleSetPoints: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val doublePointPoints: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val singleGamePoints: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val singleSetPoints: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val singlePointPoints: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val tournaments: List<Tournament> = emptyList(),
    val games: List<Game> = emptyList(),
    val searchBarText: String = "",
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val availableTags: List<Tag> = emptyList(),
    val tags: List<Tag> = emptyList()
)