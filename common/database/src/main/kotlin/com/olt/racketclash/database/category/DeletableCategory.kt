package com.olt.racketclash.database.category

data class DeletableCategory(
    val id: Long,
    val name: String,
    val tournamentId: Long,
    val players: Int,
    val finished: Boolean,
    val deletable: Boolean
)

internal fun SelectFilteredAndOrdered.toDeletableCategory() =
    DeletableCategory(
        id = id,
        name = name,
        tournamentId = tournamentId,
        players = players.toInt(),
        finished = false,
        deletable = deletable == 0L
    )

internal fun SelectSingle.toDeletableCategory() =
    DeletableCategory(
        id = id,
        name = name,
        tournamentId = tournamentId,
        players = 0,
        finished = false,
        deletable = deletable == 0L
    )