package com.olt.racketclash.addorupdatecategory

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.ViewModelState

class AddOrUpdateCategoryModel(
    private val database: Database,
    private val categoryId: Long?,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    fun updateName(newName: String) =
        updateState { copy(name = newName, isSavable = newName.isNotBlank()) }

    fun save() =
        onIO {
            //save to database
        }
}