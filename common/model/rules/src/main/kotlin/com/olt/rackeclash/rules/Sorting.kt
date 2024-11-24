package com.olt.rackeclash.rules

sealed class Sorting {
    data object NameAsc : Sorting()
    data object NameDesc : Sorting()
}