package com.olt.racketclash.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    dropDownItems: List<T>,
    onDropDownItemClick: (T) -> Unit,
    tags: List<T>,
    onTagRemove: (T) -> Unit,
    tagText: @Composable (T) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        DropDownTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = if (tags.isEmpty()) 5.dp else 0.dp),
            label = "Filter",
            text = text,
            onTextChange = onTextChange,
            dropDownItems = dropDownItems,
            dropDownItemText = tagText,
            onItemClick = onDropDownItemClick
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            tags.forEach { tag ->
                InputChip(
                    modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand),
                    selected = false,
                    onClick = { onTagRemove(tag) },
                    label = { tagText(tag) },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        trailingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.size(InputChipDefaults.AvatarSize),
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove"
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    dropDownItems: @Composable ColumnScope.() -> Unit,
    tags: @Composable FlowRowScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SearchBarTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 5.dp),
            label = "Filter",
            text = text,
            onTextChange = onTextChange,
            dropDownItems = dropDownItems
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), content = tags)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    text: String,
    readOnly: Boolean = false,
    onTextChange: (String) -> Unit,
    dropDownItems: @Composable ColumnScope.() -> Unit
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
            readOnly = readOnly,
            onValueChange = onTextChange,
            label = { Text(label) },
            singleLine = true
        )

        ExposedDropdownMenu(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            content = dropDownItems
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarMenuItem(
    name: String,
    text: String? = null,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        modifier = Modifier
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline)
            .background(color = MaterialTheme.colorScheme.primary)
            .pointerHoverIcon(icon = PointerIcon.Hand),
        text = { Tag(name = name, text = text) },
        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
        colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.onPrimary),
        onClick = onClick
    )
}

@Composable
fun SearchBarTagChip(
    name: String,
    text: String? = null,
    onRemove: () -> Unit
) {
    InputChip(
        modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand),
        selected = false,
        onClick = onRemove,
        label = { Tag(name = name, text = text) },
        colors = InputChipDefaults.inputChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            trailingIconColor = MaterialTheme.colorScheme.primary
        ),
        trailingIcon = {
            Icon(
                modifier = Modifier.size(InputChipDefaults.AvatarSize),
                imageVector = Icons.Default.Close,
                contentDescription = "Remove"
            )
        }
    )
}