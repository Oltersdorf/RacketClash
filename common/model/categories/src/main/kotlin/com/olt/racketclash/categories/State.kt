package com.olt.racketclash.categories

data class State(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val searchBarText: String = "",
    val availableTags: List<Tag> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val currentPage: Int = 1,
    val lastPage: Int = 1
)