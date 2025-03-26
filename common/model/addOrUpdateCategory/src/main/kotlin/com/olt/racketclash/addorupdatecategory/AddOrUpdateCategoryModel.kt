package com.olt.racketclash.addorupdatecategory

import com.olt.racketclash.database.api.CategoryDatabase
import com.olt.racketclash.database.api.CategoryType
import com.olt.racketclash.state.ViewModelState

class AddOrUpdateCategoryModel(
    private val database: CategoryDatabase,
    private val categoryId: Long?,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    init {
        onIO {
            if (categoryId == null)
                updateState { copy(isLoading = false) }
            else {
                val category = database.selectSingle(id = categoryId)

                updateState {
                    copy(
                        isLoading = false,
                        isSavable = true,
                        category = category
                    )
                }
            }
        }
    }

    fun updateName(newName: String) =
        updateState { copy(category = category.copy(name = newName), isSavable = newName.isNotBlank()) }

    fun updateType(newType: CategoryType) =
        updateState { copy(category = category.copy(type = newType)) }

    fun save(onComplete: () -> Unit = {}) =
        onIO {
            updateState { copy(isLoading = true) }

            if (categoryId == null)
                database.add(category = state.value.category.copy(tournamentId = tournamentId))
            else
                database.update(category = state.value.category)

            updateState { copy(isLoading = true) }

            onMain { onComplete() }
        }
}