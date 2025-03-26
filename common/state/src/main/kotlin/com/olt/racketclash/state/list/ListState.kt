package com.olt.racketclash.state.list

data class ListState<Item, Filter, Sorting>(
    val isLoading: Boolean = true,
    val maxSize: Int = 50,
    val items: List<Item> = emptyList(),
    val filter: Filter,
    val sorting: Sorting,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)