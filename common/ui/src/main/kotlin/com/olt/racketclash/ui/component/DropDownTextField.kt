package com.olt.racketclash.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    text: String,
    onTextChange: (String) -> Unit,
    dropDownItems: List<T>,
    dropDownItemText: @Composable (T) -> Unit,
    onItemClick: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable).fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            label = { Text(label) },
            singleLine = true
        )

        ExposedDropdownMenu(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dropDownItems.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.outline)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .pointerHoverIcon(icon = PointerIcon.Hand),
                    text = { dropDownItemText(item) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.onPrimary),
                    onClick = {
                        onItemClick(item)
                        expanded = false
                    }
                )
            }
        }
    }
}