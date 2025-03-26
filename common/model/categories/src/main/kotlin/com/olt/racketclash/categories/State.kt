package com.olt.racketclash.categories

import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.CategorySorting

data class State(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(finished = true),
    val tags: Tags = Tags(),
    val sorting: CategorySorting = CategorySorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)