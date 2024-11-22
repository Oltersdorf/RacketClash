package com.olt.racketclash.state

sealed class SortDirection {
    data object Ascending : SortDirection()
    data object Descending : SortDirection()
}