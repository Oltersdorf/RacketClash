package com.olt.racketclash.ui.base.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun Link(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontSize: TextUnit = LocalTextStyle.current.fontSize,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = text,
        modifier = modifier
            .clickable(onClick = onClick, interactionSource = interactionSource, indication = null)
            .pointerHoverIcon(icon = PointerIcon.Hand),
        style = style.merge(color = MaterialTheme.colorScheme.primary),
        fontSize = fontSize
    )
}