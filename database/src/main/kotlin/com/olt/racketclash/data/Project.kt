package com.olt.racketclash.data

data class Project(
    val id: Long,
    val name: String,
    val lastModified: String,
    val numberOfPlayers: Int,
    val numberOfTeams: Int
)