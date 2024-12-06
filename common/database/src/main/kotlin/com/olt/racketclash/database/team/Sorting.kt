package com.olt.racketclash.database.team

sealed class Sorting(val name: String) {
    data object NameAsc : Sorting("nameAsc")
    data object NameDesc : Sorting("nameDesc")
    data object RankAsc : Sorting("rankAsc")
    data object RankDesc : Sorting("rankDesc")
    data object SizeAsc : Sorting("sizeAsc")
    data object SizeDesc : Sorting("sizeDesc")
    data object GamesAsc : Sorting("gamesAsc")
    data object GamesDesc : Sorting("gamesDesc")
}