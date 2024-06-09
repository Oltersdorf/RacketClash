package com.olt.racketclash.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.language.Language

@Composable
fun CancelSaveButtonRow(
    language: Language,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    canSave: Boolean = true
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Spacer(modifier = Modifier.weight(1.0f))
        Button(onClick = onCancel) {
            Text(language.cancel)
        }
        Button(onClick = onSave, enabled = canSave) {
            Text(language.save)
        }
    }
}