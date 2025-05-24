package com.olt.racketclash.ui.base.layout

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Overlay(
    visible: Boolean,
    durationMillis: Int = 500,
    onDismiss: () -> Unit,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    AnimatedVisibility(
        label = "overlayBackground",
        visible = visible,
        enter = fadeIn(
            initialAlpha = 0.0f,
            animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
        ),
        exit = fadeOut(
            targetAlpha = 0.0f,
            animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().onClick(onClick = onDismiss),
            color = MaterialTheme.colorScheme.scrim.copy(0.3f)
        ) {}
    }

    AnimatedVisibility(
        label = "overlay",
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = durationMillis, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = durationMillis, easing = LinearOutSlowInEasing)
        )
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Box(
                modifier = Modifier.padding(50.dp),
                contentAlignment = contentAlignment
            ) {
                content()
            }
        }
    }
}

@Composable
fun FilterFormOverlay(
    visible: Boolean,
    durationMillis: Int = 500,
    dismissOverlay: () -> Unit,
    onConfirm: () -> Unit,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable ColumnScope.() -> Unit
) {
    Overlay(
        visible = visible,
        durationMillis = durationMillis,
        onDismiss = dismissOverlay,
        contentAlignment = contentAlignment
    ) {
        Form(
            title = "Filter",
            abortButton = {
                FormButton(
                    text = "Close",
                    icon = Icons.Default.Close,
                    contentDescription = "Close",
                    onClick = dismissOverlay
                )
            },
            confirmButton = {
                FormButton(
                    text = "Filter",
                    icon = Icons.Default.Search,
                    contentDescription = "Filter"
                ) {
                    dismissOverlay()
                    onConfirm()
                }
            },
            content = content
        )
    }
}

@Composable
fun AddOrUpdateFormOverlay(
    title: String,
    visible: Boolean,
    durationMillis: Int = 500,
    dismissOverlay: () -> Unit,
    canConfirm: Boolean,
    onConfirm: () -> Unit,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable ColumnScope.() -> Unit
) {
    Overlay(
        visible = visible,
        durationMillis = durationMillis,
        onDismiss = dismissOverlay,
        contentAlignment = contentAlignment
    ) {
        Form(
            title = title,
            abortButton = {
                FormButton(
                    text = "Close",
                    icon = Icons.Default.Close,
                    contentDescription = "Close",
                    onClick = dismissOverlay
                )
            },
            confirmButton = {
                FormButton(
                    text = "Save",
                    enabled = canConfirm
                ) {
                    dismissOverlay()
                    onConfirm()
                }
            },
            content = content
        )
    }
}