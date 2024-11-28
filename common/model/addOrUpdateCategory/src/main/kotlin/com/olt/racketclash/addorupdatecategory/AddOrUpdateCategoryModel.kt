package com.olt.racketclash.addorupdatecategory

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.category.CategoryType
import com.olt.racketclash.state.ViewModelState

class AddOrUpdateCategoryModel(
    private val database: Database,
    private val categoryId: Long?,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    init {
        onIO {
            if (categoryId == null)
                updateState { copy(isLoading = false) }
            else {
                updateState {
                    copy(
                        isLoading = false,
                        isSavable = true,
                        category = database.categories.selectSingle(id = categoryId)
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
                database.categories.add(category = state.value.category.copy(tournamentId = tournamentId))
            else
                database.categories.update(category = state.value.category)

            updateState { copy(isLoading = true) }

            onMain { onComplete() }
        }
}