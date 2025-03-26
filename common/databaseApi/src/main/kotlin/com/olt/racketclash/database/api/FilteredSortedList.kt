package com.olt.racketclash.database.api

data class FilteredSortedList<Item, Filter, Sorting>(
    val totalSize: Long,
    val fromIndex: Long,
    val toIndex: Long,
    val items: List<Item>,
    val filter: Filter,
    val sorting: Sorting
)