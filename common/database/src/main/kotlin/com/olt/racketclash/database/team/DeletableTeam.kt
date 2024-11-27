package com.olt.racketclash.database.team

data class DeletableTeam(
    val id: Long,
    val name: String,
    val size: Int,
    val winRatioSingle: Triple<Int, Int, Int>,
    val winRatioDouble: Triple<Int, Int, Int>,
    val deletable: Boolean
)

internal fun SelectFilteredAndOrdered.toDeletableTeam() =
    DeletableTeam(
        id = id,
        name = name,
        size = 0,
        winRatioSingle = Triple(0, 0, 0),
        winRatioDouble = Triple(0, 0, 0),
        deletable = deletable == 1L
    )

internal fun SelectSingle.toDeletableTeam() =
    DeletableTeam(
        id = id,
        name = name,
        size = 0,
        winRatioSingle = Triple(0, 0, 0),
        winRatioDouble = Triple(0, 0, 0),
        deletable = deletable == 1L
    )