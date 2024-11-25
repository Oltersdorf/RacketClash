package com.olt.racketclash.database.player

data class DeletablePlayer(
    val id: Long,
    val name: String,
    val birthYear: Int,
    val club: String,
    val numberOfTournaments: Int,
    val goldMedals: Int,
    val silverMedals: Int,
    val bronzeMedals: Int,
    val winRatioSingle: Triple<Int, Int, Int>,
    val winRatioDouble: Triple<Int, Int, Int>,
    val deletable: Boolean
)

internal fun SelectFilteredAndOrdered.toDeletablePlayer() =
    DeletablePlayer(
        id = id,
        name = name,
        birthYear = birthYear,
        club = club,
        numberOfTournaments = numberOfTournaments.toInt(),
        goldMedals = goldMedals.toInt(),
        silverMedals = silverMedals.toInt(),
        bronzeMedals = bronzeMedals.toInt(),
        winRatioSingle = Triple(0, 0, 0),
        winRatioDouble = Triple(0, 0, 0),
        deletable = deletable != 0L
    )

internal fun SelectSingle.toDeletablePlayer() =
    DeletablePlayer(
        id = id,
        name = name,
        birthYear = birthYear,
        club = club,
        numberOfTournaments = 0,
        goldMedals = 0,
        silverMedals = 0,
        bronzeMedals = 0,
        winRatioSingle = Triple(0, 0, 0),
        winRatioDouble = Triple(0, 0, 0),
        deletable = deletable != 0L
    )