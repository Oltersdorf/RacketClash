package com.olt.racketclash.database.rule

sealed class Sorting(val name: String) {
    data object NameAsc : Sorting("nameAsc")
    data object NameDesc : Sorting("nameDesc")
}