package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
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
    var isLoading by remember { mutableStateOf(true) }
    var isSavable by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    Form(
        title = categoryName ?: "New category",
        isLoading = isLoading,
        isSavable = isSavable,
        onSave = {
            navigateBack()
        }
    ) {
        FormTextField(value = name, label = "Name", isError = !isSavable) {
            name = it
            isSavable = name.isNotBlank()
        }
    }
}