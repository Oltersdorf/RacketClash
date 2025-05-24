package com.olt.racketclash.state.list

import com.olt.racketclash.database.api.Validateable

data class ListState<Item: Validateable, Filter, Sorting>(
    val isLoading: Boolean = true,
    val maxSize: Int = 50,
    val items: List<Item> = emptyList(),
    val addItem: Item,
    val canAdd: Boolean = addItem.validate(),
    val filter: Filter,
    val filterUpdate: Filter = filter,
    val sorting: Sorting,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)