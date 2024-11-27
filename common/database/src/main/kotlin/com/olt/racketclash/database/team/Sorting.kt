package com.olt.racketclash.database.team

sealed class Sorting(val name: String) {
    data object NameAsc : Sorting("nameAsc")
    data object NameDesc : Sorting("nameDesc")
    data object SizeAsc : Sorting("sizeAsc")
    data object SizeDesc : Sorting("sizeDesc")
    data object SinglesAsc : Sorting("singlesAsc")
    data object SinglesDesc : Sorting("singlesDesc")
    data object DoublesAsc : Sorting("doublesAsc")
    data object DoublesDesc : Sorting("doublesDesc")
}