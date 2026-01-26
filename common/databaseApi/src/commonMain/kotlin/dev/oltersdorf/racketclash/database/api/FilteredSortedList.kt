package dev.oltersdorf.racketclash.database.api

data class FilteredSortedList<out Item, Filter, Sorting>(
    val totalItemsInDatabase: Long,
    val fromIndex: Long,
    val limit: Int,
    val items: List<Item>,
    val filter: Filter,
    val sorting: Sorting
)