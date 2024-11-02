package com.olt.racketclash.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> SearchBar(
    modifier: Modifier = Modifier,
    label: String = "",
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
            label = label,
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