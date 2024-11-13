package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.layout.Form
import com.olt.racketclash.ui.layout.FormTextField

@Composable
internal fun AddOrUpdateTeam(
    database: Database,
    teamId: Long?,
    teamName: String?,
    tournamentId: Long,
    navigateBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var isSavable by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    Form(
        title = teamName ?: "New team",
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