package com.olt.racketclash.ui.view

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.olt.racketclash.addorupdatecategory.AddOrUpdateCategoryModel
import com.olt.racketclash.database.api.CategoryType
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.ui.base.layout.Form
import com.olt.racketclash.ui.base.layout.FormButton
import com.olt.racketclash.ui.base.layout.FormDropDownTextField
import com.olt.racketclash.ui.base.layout.FormTextField

@Composable
internal fun AddOrUpdateCategory(
    database: Database,
    categoryId: Long?,
    categoryName: String?,
    tournamentId: Long,
    navigateBack: () -> Unit
) {
    val model = remember { AddOrUpdateCategoryModel(
        database = database.categories,
        categoryId = categoryId,
        tournamentId = tournamentId
    ) }
    val state by model.state.collectAsState()

    Form(
        title = categoryName ?: "New category",
        isLoading = state.isLoading,
        confirmButton = {
            FormButton(
                text = "Save",
                enabled = state.isSavable
            ) { model.save(onComplete = navigateBack) }
        }
    ) {
        FormTextField(
            value = state.category.name,
            label = "Name",
            isError = !state.isSavable,
            onValueChange = model::updateName
        )

        FormDropDownTextField(
            text = state.category.type.text(),
            label = "Type",
            readOnly = true,
            dropDownItems = listOf(CategoryType.Custom, CategoryType.Table, CategoryType.Tree),
            dropDownItemText = { Text(it.text()) },
            onItemClicked = model::updateType
        )
    }
}

private fun CategoryType.text(): String =
    when (this) {
        CategoryType.Custom -> "Custom"
        CategoryType.Table -> "List"
        CategoryType.Tree -> "Tree"
    }