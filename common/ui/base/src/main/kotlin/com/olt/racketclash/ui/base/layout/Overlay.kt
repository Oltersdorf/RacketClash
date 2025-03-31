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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <State> BoxScope.Overlay(
    defaultState: State,
    state: State? = defaultState,
    visible: Boolean,
    durationMillis: Int = 500,
    onDismiss: () -> Unit,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.(State, (State.() -> State) -> Unit) -> Unit
) {
    var stateHolder by remember(key1 = visible) { mutableStateOf(state ?: defaultState) }

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
                content(stateHolder) { stateHolder = it(stateHolder) }
            }
        }
    }
}

@Composable
fun <State> BoxScope.FilterFormOverlay(
    filterState: State,
    visible: Boolean,
    durationMillis: Int = 500,
    dismissOverlay: () -> Unit,
    onFilter: (State) -> Unit,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable ColumnScope.(State, (State.() -> State) -> Unit) -> Unit
) {
    Overlay(
        defaultState = filterState,
        visible = visible,
        durationMillis = durationMillis,
        onDismiss = dismissOverlay,
        contentAlignment = contentAlignment
    ) { state, updateState ->
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
                    onFilter(state)
                }
            }
        ) {
            content(state, updateState)
        }
    }
}

@Composable
fun <State> BoxScope.AddOrUpdateFormOverlay(
    defaultItemState: State,
    itemState: State? = defaultItemState,
    visible: Boolean,
    durationMillis: Int = 500,
    dismissOverlay: () -> Unit,
    canConfirm: (State) -> Boolean,
    onConfirm: (State) -> Unit,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable ColumnScope.(State, (State.() -> State) -> Unit) -> Unit
) {
    Overlay(
        defaultState = defaultItemState,
        state = itemState,
        visible = visible,
        durationMillis = durationMillis,
        onDismiss = dismissOverlay,
        contentAlignment = contentAlignment
    ) { state, updateState ->
        Form(
            title = if (itemState == null) "Add" else "Update",
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
                    enabled = canConfirm(state)
                ) {
                    dismissOverlay()
                    onConfirm(state)
                }
            }
        ) {
            content(state, updateState)
        }
    }
}