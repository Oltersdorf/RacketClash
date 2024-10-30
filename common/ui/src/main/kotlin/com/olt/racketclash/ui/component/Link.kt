package com.olt.racketclash.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle

@Composable
fun Link(
    text: String,
    style: TextStyle,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = text,
        modifier = Modifier
            .clickable(onClick = onClick, interactionSource = interactionSource, indication = null)
            .pointerHoverIcon(icon = PointerIcon.Hand),
        style = style.merge(color = MaterialTheme.colorScheme.primary)
    )
}