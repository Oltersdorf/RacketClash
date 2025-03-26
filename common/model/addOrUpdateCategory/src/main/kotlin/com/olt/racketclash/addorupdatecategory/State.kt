package com.olt.racketclash.addorupdatecategory

import com.olt.racketclash.database.api.Category


data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val category: Category = Category()
)