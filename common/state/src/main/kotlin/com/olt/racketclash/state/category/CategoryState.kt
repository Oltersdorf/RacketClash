package com.olt.racketclash.state.category

import com.olt.racketclash.database.api.Category

data class CategoryState(
    val isLoading: Boolean = true,
    val category: Category = Category()
)