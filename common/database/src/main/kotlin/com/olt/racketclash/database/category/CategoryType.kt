package com.olt.racketclash.database.category

sealed class CategoryType {
    data object Custom : CategoryType()
    data object Tree : CategoryType()
    data object List : CategoryType()
}