package com.olt.racketclash.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Tag(
    name: String,
    text: String? = null
) {
    if (text != null) {
        Row {
            Text(text = "$name: ", fontWeight = FontWeight.Bold)
            Text(text = "\"$text\"")
        }
    }
    else
        Text(text = name, fontWeight = FontWeight.Bold)
}

@Composable
fun SearchChip(
    name: String,
    text: String,
    onClick: () -> Unit
) {
    InputChip(
        modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand),
        colors = InputChipDefaults.inputChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        selected = false,
        onClick = onClick,
        label = { Tag(name = name, text = text) },
        trailingIcon = {
            Icon(
                modifier = Modifier.size(InputChipDefaults.AvatarSize),
                imageVector = Icons.Default.Close,
                contentDescription = "Remove"
            )
        }
    )
}