package com.olt.racketclash.ui.component

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownIconButton(
    items: List<T>,
    textMapper: (T) -> String,
    onClick: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        IconButton(
            modifier = Modifier.menuAnchor(),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            ),
            onClick = {  }
        ) {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }

        ExposedDropdownMenu(
            modifier = Modifier.width(IntrinsicSize.Max),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(text = textMapper(it)) },
                    onClick = {
                        onClick(it)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}