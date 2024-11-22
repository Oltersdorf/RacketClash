package com.olt.rackeclash.rules

data class State(
    val isLoading: Boolean = true,
    val rules: List<Rule> = emptyList(),
    val searchBarText: String = "",
    val availableTags: List<Tag> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val currentPage: Int = 1,
    val lastPage: Int = 1
)