package com.olt.racketclash.player

data class Tournament(
    val id: Long,
    val name: String,
    val teamId: Long?,
    val teamName: String?,
    val categories: List<Category>
)