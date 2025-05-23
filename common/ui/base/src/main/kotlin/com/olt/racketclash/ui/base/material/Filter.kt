package com.olt.racketclash.ui.base.material

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
internal fun Tag(
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
fun FilterChip(
    name: String,
    text: String,
    onClick: () -> Unit
) {
    InputChip(
        modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand),
        colors = InputChipDefaults.inputChipColors(
            containerColor = MaterialTheme.colorScheme.primary,
            trailingIconColor = MaterialTheme.colorScheme.onPrimary,
            labelColor = MaterialTheme.colorScheme.onPrimary
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