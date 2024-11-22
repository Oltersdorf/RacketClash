package com.olt.racketclash.categories

data class Category(
    val id: Long,
    val name: String,
    val players: Int,
    val finished: Boolean
)