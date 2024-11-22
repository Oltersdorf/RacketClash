package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.racketclash.addorupdatecategory.AddOrUpdateCategoryModel
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.layout.Form
import com.olt.racketclash.ui.layout.FormTextField

@Composable
internal fun AddOrUpdateCategory(
    database: Database,
    categoryId: Long?,
    categoryName: String?,
    tournamentId: Long,
    navigateBack: () -> Unit
) {
    val model = remember { AddOrUpdateCategoryModel(
        database = database,
        categoryId = categoryId,
        tournamentId = tournamentId
    ) }
    val state by model.state.collectAsState()

    Form(
        title = categoryName ?: "New category",
        isLoading = state.isLoading,
        isSavable = state.isSavable,
        onSave = {
            model.save()
            navigateBack()
        }
    ) {
        FormTextField(
            value = state.name,
            label = "Name",
            isError = !state.isSavable,
            onValueChange = model::updateName
        )
    }
}