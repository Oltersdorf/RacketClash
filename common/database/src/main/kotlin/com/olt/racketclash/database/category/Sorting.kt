package com.olt.racketclash.database.category

sealed class Sorting(val name: String) {
    data object NameAsc : Sorting("nameAsc")
    data object NameDesc : Sorting("nameDesc")
    data object TypeAsc : Sorting("typeAsc")
    data object TypeDesc : Sorting("typeDesc")
    data object PlayersAsc : Sorting("playersAsc")
    data object PlayersDesc : Sorting("playersDesc")
    data object StatusAsc : Sorting("statusAsc")
    data object StatusDesc : Sorting("statusDesc")
}