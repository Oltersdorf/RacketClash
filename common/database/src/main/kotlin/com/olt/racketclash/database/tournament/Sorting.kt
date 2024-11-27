package com.olt.racketclash.database.tournament

sealed class Sorting(val name: String) {
    data object NameAsc : Sorting("nameAsc")
    data object NameDesc : Sorting("nameDesc")
    data object LocationAsc : Sorting("locationAsc")
    data object LocationDesc : Sorting("locationDesc")
    data object CourtsAsc : Sorting("courtsAsc")
    data object CourtsDesc : Sorting("courtsDesc")
    data object StartDateTimeAsc : Sorting("startDateTimeAsc")
    data object StartDateTimeDesc : Sorting("startDateTimeDesc")
    data object EndDateTimeAsc : Sorting("endDateTimeAsc")
    data object EndDateTimeDesc : Sorting("endDateTimeDesc")
    data object PlayersAsc : Sorting("playersAsc")
    data object PlayersDesc : Sorting("playersDesc")
}