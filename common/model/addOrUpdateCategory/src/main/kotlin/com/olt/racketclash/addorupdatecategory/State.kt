package com.olt.racketclash.addorupdatecategory

import com.olt.racketclash.database.category.CategoryType
import com.olt.racketclash.database.category.DeletableCategory

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val category: DeletableCategory = DeletableCategory(
        id = 0L,
        name = "",
        type = CategoryType.Custom,
        tournamentId = 0L,
        players = 0,
        finished = false,
        deletable = false
    )
)