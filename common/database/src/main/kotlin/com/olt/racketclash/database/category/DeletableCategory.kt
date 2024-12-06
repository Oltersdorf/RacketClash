package com.olt.racketclash.database.category

import com.olt.racketclash.database.table.category.SelectFilteredAndOrdered
import com.olt.racketclash.database.table.category.SelectSingle

data class DeletableCategory(
    val id: Long,
    val name: String,
    val type: CategoryType,
    val tournamentId: Long,
    val players: Int,
    val finished: Boolean,
    val deletable: Boolean
)

internal fun SelectFilteredAndOrdered.toDeletableCategory() =
    DeletableCategory(
        id = id,
        name = name,
        type = type,
        tournamentId = tournamentId,
        players = players.toInt(),
        finished = false,
        deletable = deletable == 0L
    )

internal fun SelectSingle.toDeletableCategory() =
    DeletableCategory(
        id = id,
        name = name,
        type = type,
        tournamentId = tournamentId,
        players = 0,
        finished = false,
        deletable = deletable == 0L
    )