package com.olt.racketclash.state.category

import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.CategoryDatabase
import com.olt.racketclash.state.ViewModelState

class CategoryModel(
    private val categoryDatabase: CategoryDatabase,
    categoryId: Long
) : ViewModelState<CategoryState>(initialState = CategoryState()) {

    init {
        onIO {
            val category = categoryDatabase.selectSingle(id = categoryId)

            updateState {
                copy(
                    isLoading = false,
                    category = category
                )
            }
        }
    }

    fun updatePlayer(category: Category) {
        onIO {
            updateState { copy(category = category) }
            categoryDatabase.update(category = category)
        }
    }
}