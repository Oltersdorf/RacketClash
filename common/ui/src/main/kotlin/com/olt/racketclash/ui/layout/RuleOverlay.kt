package com.olt.racketclash.ui.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.rule.Filter
import com.olt.racketclash.database.rule.emptyRule
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.ui.material.FilterChip
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun FilterRuleTags(
    filter: Filter,
    applyFilter: (Filter) -> Unit
) {
    if (filter.name.isNotBlank()) FilterChip(name = "Name", text = filter.name) {
        applyFilter(filter.copy(name = ""))
    }
}

@Composable
fun FilterRuleOverlay(
    filter: Filter,
    applyFilter: (Filter) -> Unit,
    visible: Boolean,
    dismissOverlay: () -> Unit
) {
    Overlay(
        visible = visible,
        onDismiss = dismissOverlay
    ) {
        var name by remember { mutableStateOf(filter.name) }

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shadowElevation = 1.dp
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
                        applyFilter(filter.copy(name = name))
                    }
                }
            ) {
                FormTextField(value = name, label = "Name") { name = it }
            }
        }
    }
}

@Composable
fun AddOrUpdateRuleOverlay(
    visible: Boolean,
    rule: FilteredAndOrderedRule = emptyRule(),
    onConfirm: (FilteredAndOrderedRule) -> Unit,
    dismissOverlay: () -> Unit
) {
    Overlay(
        visible = visible,
        onDismiss = dismissOverlay
    ) {
        var name by remember { mutableStateOf(rule.name) }
        var maxSets by remember { mutableStateOf(rule.maxSets) }
        var winSets by remember { mutableStateOf(rule.winSets) }
        var maxPoints by remember { mutableStateOf(rule.maxPoints) }
        var winPoints by remember { mutableStateOf(rule.winPoints) }
        var pointsDifference by remember { mutableStateOf(rule.pointsDifference) }
        var gamePointsForWin by remember { mutableStateOf(rule.gamePointsForWin) }
        var gamePointsForLose by remember { mutableStateOf(rule.gamePointsForLose) }
        var gamePointsForDraw by remember { mutableStateOf(rule.gamePointsForDraw) }
        var gamePointsForRest by remember { mutableStateOf(rule.gamePointsForDraw) }
        var setPointsForRest by remember { mutableStateOf(rule.setPointsForRest) }
        var pointPointsForRest by remember { mutableStateOf(rule.pointPointsForRest) }

        Form(
            title = "New rule",
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
                    enabled = name.isNotBlank()
                ) {
                    dismissOverlay()
                    onConfirm(
                        rule.copy(
                            name = name,
                            maxSets = maxSets,
                            winSets = winSets,
                            maxPoints = maxPoints,
                            winPoints = winPoints,
                            pointsDifference = pointsDifference,
                            gamePointsForWin = gamePointsForWin,
                            gamePointsForLose = gamePointsForLose,
                            gamePointsForDraw = gamePointsForDraw,
                            gamePointsForRest = gamePointsForRest,
                            setPointsForRest = setPointsForRest,
                            pointPointsForRest = pointPointsForRest
                        )
                    )
                }
            }
        ) {
            FormTextField(value = name, label = "Name", isError = name.isBlank()) { name = it }

            FormRow {
                FormNumberSelector(
                    value = maxSets,
                    label = "Max sets",
                    range = 1..Int.MAX_VALUE,
                    onUp = {
                        maxSets = it
                        winSets = max(winSets, ceil(it.toDouble() / 2).roundToInt())
                    },
                    onDown = {
                        maxSets = it
                        winSets = min(it, winSets)
                    }
                )

                FormNumberSelector(
                    value = winSets,
                    label = "Win sets",
                    range = 1..Int.MAX_VALUE,
                    onUp = {
                        winSets = it
                        maxSets = max(maxSets, it)
                    },
                    onDown = {
                        winSets = it
                        maxSets = min(maxSets, it * 2)
                    }
                )
            }

            FormRow {
                FormNumberSelector(
                    value = maxPoints,
                    label = "Max points",
                    range = 1..Int.MAX_VALUE,
                    onUp = { maxPoints = it },
                    onDown = {
                        maxPoints = it
                        winPoints = min(winPoints, it)
                        pointsDifference = min(pointsDifference, it)
                    }
                )

                FormNumberSelector(
                    value = winPoints,
                    label = "Win points",
                    range = 1..Int.MAX_VALUE,
                    onUp = {
                        winPoints = it
                        maxPoints = max(maxPoints, it)
                    },
                    onDown = {
                        winPoints = it
                        pointsDifference = min(pointsDifference, it)
                    }
                )

                FormNumberSelector(
                    value = pointsDifference,
                    label = "Points difference",
                    range = 0..Int.MAX_VALUE,
                    onUp = {
                        pointsDifference = it
                        winPoints = max(winPoints, it)
                        maxPoints = max(maxPoints, it)
                    },
                    onDown = { pointsDifference = it }
                )
            }

            FormRow {
                FormNumberSelector(
                    value = gamePointsForWin,
                    label = "Game points for win",
                    range = 0..Int.MAX_VALUE,
                    onUp = { gamePointsForWin = it },
                    onDown = { gamePointsForWin = it }
                )

                FormNumberSelector(
                    value = gamePointsForLose,
                    label = "Game points for lose",
                    range = 0..Int.MAX_VALUE,
                    onUp = { gamePointsForLose = it },
                    onDown = { gamePointsForLose = it }
                )

                FormNumberSelector(
                    value = gamePointsForDraw,
                    label = "Game points for draw",
                    range = 0..Int.MAX_VALUE,
                    onUp = { gamePointsForDraw = it },
                    onDown = { gamePointsForDraw = it }
                )
            }

            FormRow {
                FormNumberSelector(
                    value = gamePointsForRest,
                    label = "Game points for rest",
                    range = 0..Int.MAX_VALUE,
                    onUp = { gamePointsForRest = it },
                    onDown = { gamePointsForRest = it }
                )

                FormNumberSelector(
                    value = setPointsForRest,
                    label = "Set points for rest",
                    range = 0..Int.MAX_VALUE,
                    onUp = { setPointsForRest = it },
                    onDown = { setPointsForRest = it }
                )

                FormNumberSelector(
                    value = pointPointsForRest,
                    label = "Point points for rest",
                    range = 0..Int.MAX_VALUE,
                    onUp = { pointPointsForRest = it },
                    onDown = { pointPointsForRest = it }
                )
            }
        }
    }
}