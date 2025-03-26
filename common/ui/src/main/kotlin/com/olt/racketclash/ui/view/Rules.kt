package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Rule
import com.olt.racketclash.database.api.RuleFilter
import com.olt.racketclash.database.api.RuleSorting
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.state.rule.RuleTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.material.*
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.layout.RacketClashScaffold

@Composable
internal fun Rules(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { RuleTableModel(database = database.rules) }
    val state by model.state.collectAsState()
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showAddOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Rules",
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
            FilterRuleOverlay(
                filter = state.filter,
                applyFilter = model::filter,
                visible = showFilterOverlay
            ) { showFilterOverlay = false }

            AddOrUpdateRuleOverlay(
                visible = showAddOverlay,
                onConfirm = { model.add(item = it) }
            ) { showAddOverlay = false }
        }
    ) {
        Body(
            state = state,
            sort = model::sort,
            filter = model::filter,
            delete = model::delete,
            selectPage = model::selectPage,
            navigateTo = navigateTo
        )
    }
}

@Composable
private fun Body(
    state: ListState<Rule, RuleFilter, RuleSorting>,
    sort: (RuleSorting) -> Unit,
    filter: (RuleFilter) -> Unit,
    delete: (Rule) -> Unit,
    selectPage: (Int) -> Unit,
    navigateTo: (View) -> Unit
) {
    Column {
        FilterRuleTags(filter = state.filter, applyFilter = filter)

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
                    onSort = sort,
                    onDelete = delete
                )
            )
        }

        PageSelector(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            currentPage = state.currentPage,
            lastPage = state.lastPage,
            onPageClicked = selectPage
        )
    }
}

private fun columns(
    navigateTo: (View) -> Unit,
    onSort: (RuleSorting) -> Unit,
    onDelete: (Rule) -> Unit
): List<LazyTableColumn<Rule>> =
    listOf(
        LazyTableColumn.Link(name = "Name", text = { it.name }, weight = 0.6f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(RuleSorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(RuleSorting.NameDesc)
            }
        }) {
            navigateTo(View.Rule(id = it.id))
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
