package com.olt.racketclash.team

data class State(
    val isLoading: Boolean = true,
    val players: List<Player> = emptyList(),
    val numberOfPlayers: Int = 0,
    val doubleGamePoints: Triple<Int, Int, Int> = Triple(0,0,0),
    val doubleSetPoints: Triple<Int, Int, Int> = Triple(0,0,0),
    val doublePointPoints: Triple<Int, Int, Int> = Triple(0,0,0),
    val singleGamePoints: Triple<Int, Int, Int> = Triple(0,0,0),
    val singleSetPoints: Triple<Int, Int, Int> = Triple(0,0,0),
    val singlePointPoints: Triple<Int, Int, Int> = Triple(0,0,0),
    val searchBarText: String = "",
    val availableTags: List<Tag> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val currentPage: Int = 1,
    val lastPage: Int = 1
)