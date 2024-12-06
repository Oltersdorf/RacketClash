package com.olt.rackeclash.rules

import com.olt.racketclash.database.rule.Sorting
import com.olt.racketclash.database.table.FilteredAndOrderedRule

data class State(
    val isLoading: Boolean = true,
    val rules: List<FilteredAndOrderedRule> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(),
    val tags: Tags = Tags(),
    val sorting: Sorting = Sorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)