package com.olt.racketclash.state.list

data class ListState<Item, Sorting, Filter>(
    val isLoading: Boolean = true,
    val maxSize: Int = 50,
    val items: List<Item> = emptyList(),
    val sorting: Sorting,
    val filter: Filter,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)