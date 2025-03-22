package com.olt.racketclash.state.list

data class ListState<T, S>(
    val isLoading: Boolean = true,
    val maxSize: Int = 50,
    val items: List<T> = emptyList(),
    val sorting: S,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)