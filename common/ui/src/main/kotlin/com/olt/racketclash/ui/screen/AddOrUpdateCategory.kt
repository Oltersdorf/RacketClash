package com.olt.racketclash.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.olt.racketclash.addorupdatecategory.AddOrUpdateCategoryModel
import com.olt.racketclash.database.Database
import com.olt.racketclash.database.category.CategoryType
import com.olt.racketclash.ui.layout.Form
import com.olt.racketclash.ui.layout.FormDropDownTextField
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
        onSave = { model.save(onComplete = navigateBack) }
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
            dropDownItems = listOf(CategoryType.Custom, CategoryType.List, CategoryType.Tree),
            dropDownItemText = { Text(it.text()) },
            onItemClicked = model::updateType
        )
    }
}

private fun CategoryType.text(): String =
    when (this) {
        CategoryType.Custom -> "Custom"
        CategoryType.List -> "List"
        CategoryType.Tree -> "Tree"
    }