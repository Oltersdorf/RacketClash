package com.olt.rackeclash.rules

import com.olt.racketclash.database.rule.Sorting
import com.olt.racketclash.database.table.FilteredAndOrderedRule

data class State(
    val isLoading: Boolean = true,
    val rules: List<FilteredAndOrderedRule> = emptyList(),
    val nameSearch: String = "",
    val sorting: Sorting = Sorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)