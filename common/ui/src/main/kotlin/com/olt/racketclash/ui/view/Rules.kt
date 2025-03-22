package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.rule.Sorting
import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.Filter
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.state.rule.RuleListModel
import com.olt.racketclash.state.rule.RulesModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.component.*
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.layout.RacketClashScaffold
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
internal fun Rules(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { RulesModel(database = database) }
    val state by model.state.collectAsState()
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showAddOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Rules",
        headerContent = { FilterTags(filter = state.filter, applyFilter = model::applyFilter) },
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Search,
                contentDescription = "Filter"
            ) {
                showAddOverlay = false
                showFilterOverlay = !showFilterOverlay
            }

            SimpleIconButton(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            ) {
                showFilterOverlay = false
                showAddOverlay = !showAddOverlay
            }
        },
        overlay = {
            FilterOverlay(
                filter = state.filter,
                applyFilter = model::applyFilter,
                visible = showFilterOverlay
            ) { showFilterOverlay = false }
            AddOverlay(
                model = model,
                visible = showAddOverlay
            ) { showAddOverlay = false }
        }
    ) {
        Body(model = model.rules, navigateTo = navigateTo)
    }
}

@Composable
private fun FilterTags(
    filter: Filter,
    applyFilter: (Filter) -> Unit
) {
    if (filter.name.isNotBlank()) FilterChip(name = "Name", text = filter.name) {
        applyFilter(filter.copy(name = ""))
    }
}

@Composable
private fun FilterOverlay(
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
private fun AddOverlay(
    model: RulesModel,
    visible: Boolean,
    dismissOverlay: () -> Unit
) {
    Overlay(
        visible = visible,
        onDismiss = dismissOverlay
    ) {
        var name by remember { mutableStateOf("") }
        var maxSets by remember { mutableStateOf(3) }
        var winSets by remember { mutableStateOf(2) }
        var maxPoints by remember { mutableStateOf(30) }
        var winPoints by remember { mutableStateOf(21) }
        var pointsDifference by remember { mutableStateOf(2) }
        var gamePointsForWin by remember { mutableStateOf(2) }
        var gamePointsForLose by remember { mutableStateOf(0) }
        var gamePointsForDraw by remember { mutableStateOf(1) }
        var gamePointsForRest by remember { mutableStateOf(2) }
        var setPointsForRest by remember { mutableStateOf(0) }
        var pointPointsForRest by remember { mutableStateOf(0) }

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
                    model.addRule(
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

@Composable
private fun Body(
    model: RuleListModel,
    navigateTo: (View) -> Unit
) {
    val state by model.state.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.weight(1.0f, fill = false),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shadowElevation = 1.dp
        ) {
            LazyTableWithScroll(
                items = state.items,
                isLoading = state.isLoading,
                columns = columns(
                    navigateTo = navigateTo,
                    onSort = model::sort,
                    onDelete = model::deleteItem
                )
            )
        }

        PageSelector(
            currentPage = state.currentPage,
            lastPage = state.lastPage,
            onPageClicked = model::selectPage
        )
    }
}

private fun columns(
    navigateTo: (View) -> Unit,
    onSort: (Sorting) -> Unit,
    onDelete: (FilteredAndOrderedRule) -> Unit
): List<LazyTableColumn<FilteredAndOrderedRule>> =
    listOf(
        LazyTableColumn.Link(name = "Name", text = { it.name }, weight = 0.6f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.NameDesc)
            }
        }) {
            navigateTo(View.AddOrUpdateRule(ruleName = it.name, ruleId = it.id))
        },
        LazyTableColumn.Text(name = "Sets", weight = 0.1f) {
            "${it.winSets}/${it.maxSets}"
        },
        LazyTableColumn.Text(name = "Points", weight = 0.1f) {
            "${it.winPoints}/${it.maxPoints} +/- ${it.pointsDifference}"
        },
        LazyTableColumn.Text(name = "Rating", weight = 0.1f) {
            "W:${it.gamePointsForWin} / D:${it.gamePointsForDraw} / L:${it.gamePointsForLose}"
        },
        LazyTableColumn.Text(name = "Rest", weight = 0.1f) {
            "G:${it.gamePointsForRest} / S:${it.setPointsForRest} / P:${it.pointPointsForRest}"
        },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = onDelete,
            enabled = { it.used == 0L },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )
