package com.olt.racketclash.categories

import com.olt.racketclash.database.category.DeletableCategory
import com.olt.racketclash.database.category.Sorting

data class State(
    val isLoading: Boolean = true,
    val categories: List<DeletableCategory> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(finished = true),
    val tags: Tags = Tags(),
    val sorting: Sorting = Sorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)