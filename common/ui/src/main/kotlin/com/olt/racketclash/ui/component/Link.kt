package com.olt.racketclash.ui.component

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun Link(
    text: String,
    fontSize: TextUnit,
    onClick: () -> Unit
) {
    ClickableText(
        text = AnnotatedString(text = text, spanStyle = SpanStyle(color = Color.Blue, fontSize = fontSize)),
        modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
    ) {
        onClick()
    }
}