package com.olt.rackeclash.rules

import com.olt.racketclash.database.rule.DeletableRule

data class State(
    val isLoading: Boolean = true,
    val rules: List<DeletableRule> = emptyList(),
    val searchBarText: String = "",
    val availableTags: List<Tag> = listOf(Tag.Name("")),
    val tags: List<Tag> = emptyList(),
    val sorting: Sorting = Sorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)