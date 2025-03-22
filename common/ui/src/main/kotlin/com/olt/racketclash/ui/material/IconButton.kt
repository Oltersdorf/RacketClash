package com.olt.racketclash.ui.material

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Composable
fun SimpleIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier.pointerHoverIcon(icon = PointerIcon.Hand),
        enabled = enabled,
        colors = colors,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun SimpleIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier.pointerHoverIcon(icon = PointerIcon.Hand),
        enabled = enabled,
        colors = colors,
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription
        )
    }
}