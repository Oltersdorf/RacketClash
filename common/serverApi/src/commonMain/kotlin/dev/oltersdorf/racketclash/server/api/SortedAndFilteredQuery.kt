package dev.oltersdorf.racketclash.server.api

import kotlinx.serialization.Serializable

@Serializable
data class SortedAndFilteredQuery<Filter, Sorting>(
    val filter: Filter,
    val sorting: Sorting,
    val fromIndex: Long,
    val limit: Int
)