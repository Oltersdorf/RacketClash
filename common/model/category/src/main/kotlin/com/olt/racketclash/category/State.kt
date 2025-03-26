package com.olt.racketclash.category

import com.olt.racketclash.database.api.CategoryType
import com.olt.racketclash.database.api.Game

data class State(
    val isLoading: Boolean = true,
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val type: CategoryType = CategoryType.Custom,
    val games: List<Game> = emptyList()
)