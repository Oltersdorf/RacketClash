package com.olt.racketclash.database.player

sealed class Sorting(val name: String) {
    data object NameAsc : Sorting("nameAsc")
    data object NameDesc : Sorting("nameDesc")
    data object BirthYearAsc : Sorting("birthYearAsc")
    data object BirthYearDesc : Sorting("birthYearDesc")
    data object ClubAsc : Sorting("clubAsc")
    data object ClubDesc : Sorting("clubDesc")
    data object TournamentsAsc : Sorting("tournamentsAsc")
    data object TournamentsDesc : Sorting("tournamentsDesc")
    data object MedalsAsc : Sorting("medalsAsc")
    data object MedalsDesc : Sorting("medalsDesc")
    data object SinglesAsc : Sorting("singlesAsc")
    data object SinglesDesc : Sorting("singlesDesc")
    data object DoublesAsc : Sorting("doublesAsc")
    data object DoublesDesc : Sorting("doublesDesc")
}