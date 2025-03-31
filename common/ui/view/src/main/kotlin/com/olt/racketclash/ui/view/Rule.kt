package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Rule
import com.olt.racketclash.database.api.RuleFilter
import com.olt.racketclash.database.api.RuleSorting
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.state.rule.RuleModel
import com.olt.racketclash.state.rule.RuleTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.*
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.LazyTableSortDirection
import com.olt.racketclash.ui.base.material.SimpleIconButton
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.layout.RacketClashScaffold
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private sealed class RuleTable(val index: Int, val name: String) {
    data object Tournaments : RuleTable(index = 0, name = "Tournaments")
    data object Categories : RuleTable(index = 1, name = "Categories")
    data object Games : RuleTable(index = 2, name = "Games")
}

@Composable
internal fun Rule(
    database: Database,
    id: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { RuleModel(ruleDatabase = database.rules, id = id) }
    val state by model.state.collectAsState()
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showEditOverlay by remember { mutableStateOf(false) }
    var ruleTable by remember { mutableStateOf<RuleTable>(RuleTable.Tournaments) }

    RacketClashScaffold(
        title = "Rule",
        headerContent = { Info(isLoading = state.isLoading, rule = state.rule) },
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Search,
                contentDescription = "Filter"
            ) {
                showEditOverlay = false
                showFilterOverlay = !showFilterOverlay
            }

            SimpleIconButton(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            ) {
                showFilterOverlay = false
                showEditOverlay = !showEditOverlay
            }
        },
        overlay = {
            AddOrUpdateRuleOverlay(
                visible = showEditOverlay,
                rule = state.rule,
                onConfirm = model::updateRule
            ) { showEditOverlay = false }
        }
    ) {
        RuleTableSelection(
            ruleTable = ruleTable,
            onChangeSelectedTable = { ruleTable = it },
            navigateTo = navigateTo
        )
    }
}

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
                onConfirm = model::add
            ) { showAddOverlay = false }
        }
    ) {
        RuleTable(
            state = state,
            onSort = model::sort,
            onDelete = model::delete,
            onSelectPage = model::selectPage,
            onNavigateTo = navigateTo,
            onApplyFilter = model::filter
        )
    }
}

@Composable
internal fun BoxScope.FilterRuleOverlay(
    filter: RuleFilter,
    applyFilter: (RuleFilter) -> Unit,
    visible: Boolean,
    dismissOverlay: () -> Unit
) {
    FilterFormOverlay(
        filterState = filter,
        visible = visible,
        dismissOverlay = dismissOverlay,
        onFilter = applyFilter
    ) { state, update ->
        FormTextField(value = state.name, label = "Name") { update { copy(name = it) } }
    }
}

@Composable
internal fun BoxScope.AddOrUpdateRuleOverlay(
    visible: Boolean,
    rule: Rule? = null,
    onConfirm: (Rule) -> Unit,
    dismissOverlay: () -> Unit
) {
    AddOrUpdateFormOverlay(
        defaultItemState = Rule(),
        itemState = rule,
        visible = visible,
        dismissOverlay = dismissOverlay,
        canConfirm = { it.name.isNotBlank() },
        onConfirm = onConfirm
    ) { state, update ->
        FormTextField(
            value = state.name,
            label = "Name",
            isError = state.name.isBlank(),
            onValueChange = { update { copy(name = it) } }
        )

        FormRow {
            FormNumberSelector(
                enabled = rule == null,
                value = state.maxSets,
                label = "Max sets",
                range = 1..Int.MAX_VALUE,
                onUp = {
                    update {
                        copy(
                            maxSets = it,
                            winSets = max(winSets, ceil(it.toDouble() / 2).roundToInt())
                        )
                    }
                },
                onDown = {
                    update {
                        copy(
                            maxSets = it,
                            winSets = min(it, winSets)
                        )
                    }
                }
            )

            FormNumberSelector(
                enabled = rule == null,
                value = state.winSets,
                label = "Win sets",
                range = 1..Int.MAX_VALUE,
                onUp = {
                    update {
                        copy(
                            winSets = it,
                            maxSets = max(maxSets, it)
                        )
                    }
                },
                onDown = {
                    update {
                        copy(
                            winSets = it,
                            maxSets = min(maxSets, it * 2)
                        )
                    }
                }
            )
        }

        FormRow {
            FormNumberSelector(
                enabled = rule == null,
                value = state.maxPoints,
                label = "Max points",
                range = 1..Int.MAX_VALUE,
                onUp = { update { copy(maxPoints = it) } },
                onDown = {
                    update {
                        copy(
                            maxPoints = it,
                            winPoints = min(winPoints, it),
                            pointsDifference = min(pointsDifference, it)
                        )
                    }
                }
            )

            FormNumberSelector(
                enabled = rule == null,
                value = state.winPoints,
                label = "Win points",
                range = 1..Int.MAX_VALUE,
                onUp = {
                    update {
                        copy(
                            winPoints = it,
                            maxPoints = max(maxPoints, it)
                        )
                    }
                },
                onDown = {
                    update {
                        copy(
                            winPoints = it,
                            pointsDifference = min(pointsDifference, it)
                        )
                    }
                }
            )

            FormNumberSelector(
                enabled = rule == null,
                value = state.pointsDifference,
                label = "Points difference",
                range = 0..Int.MAX_VALUE,
                onUp = {
                    update {
                        copy(
                            pointsDifference = it,
                            winPoints = max(winPoints, it),
                            maxPoints = max(maxPoints, it)
                        )
                    }
                },
                onDown = { update { copy(pointsDifference = it) } }
            )
        }

        FormRow {
            FormNumberSelector(
                enabled = rule == null,
                value = state.gamePointsForWin,
                label = "Game points for win",
                range = 0..Int.MAX_VALUE,
                onUp = { update { copy(gamePointsForWin = it) } },
                onDown = { update { copy(gamePointsForWin = it) } }
            )

            FormNumberSelector(
                enabled = rule == null,
                value = state.gamePointsForLose,
                label = "Game points for lose",
                range = 0..Int.MAX_VALUE,
                onUp = { update { copy(gamePointsForLose = it) } },
                onDown = { update { copy(gamePointsForLose = it) } }
            )

            FormNumberSelector(
                enabled = rule == null,
                value = state.gamePointsForDraw,
                label = "Game points for draw",
                range = 0..Int.MAX_VALUE,
                onUp = { update { copy(gamePointsForDraw = it) } },
                onDown = { update { copy(gamePointsForDraw = it) } }
            )
        }

        FormRow {
            FormNumberSelector(
                enabled = rule == null,
                value = state.gamePointsForRest,
                label = "Game points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = { update { copy(gamePointsForRest = it) } },
                onDown = { update { copy(gamePointsForRest = it) } }
            )

            FormNumberSelector(
                enabled = rule == null,
                value = state.setPointsForRest,
                label = "Set points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = { update { copy(setPointsForRest = it) } },
                onDown = { update { copy(setPointsForRest = it) } }
            )

            FormNumberSelector(
                enabled = rule == null,
                value = state.pointPointsForRest,
                label = "Point points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = { update { copy(pointPointsForRest = it) } },
                onDown = { update { copy(pointPointsForRest = it) } }
            )
        }
    }
}

@Composable
internal fun RuleTable(
    state: ListState<Rule, RuleFilter, RuleSorting>,
    onSort: (RuleSorting) -> Unit,
    onDelete: (Rule) -> Unit,
    onSelectPage: (Int) -> Unit,
    onNavigateTo: (View) -> Unit,
    onApplyFilter: (RuleFilter) -> Unit
) {
    FilteredLazyTable(
        state = state,
        columns = columns(
            navigateTo = onNavigateTo,
            onSort = onSort,
            onDelete = onDelete
        ),
        onPageClicked = onSelectPage
    ) {
        if (it.name.isNotBlank())
            FilterChip(name = "Name", text = it.name) { onApplyFilter(it.copy(name = "")) }
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

@Composable
private fun RuleTableSelection(
    ruleTable: RuleTable,
    onChangeSelectedTable: (RuleTable) -> Unit,
    navigateTo: (View) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TabRow(
            selectedTabIndex = ruleTable.index,
            tabs = listOf(RuleTable.Tournaments, RuleTable.Categories, RuleTable.Games),
            onTabClick = onChangeSelectedTable,
            tabText = { it.name }
        )

        when (ruleTable) {
            RuleTable.Tournaments -> {}
            RuleTable.Categories -> {}
            RuleTable.Games -> {}
        }
    }
}

@Composable
private fun Info(
    isLoading: Boolean,
    rule: Rule
) {
    Details(
        isLoading = isLoading,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        DetailSectionRow(title = rule.name) {
            Column {
                DetailText(title = "id", text = rule.id.toString())
                DetailText(title = "Sets", text = "${rule.winSets}/${rule.maxSets}")
                DetailText(title = "Rating", text = "W:${rule.gamePointsForWin} / D:${rule.gamePointsForDraw} / L:${rule.gamePointsForLose}")
            }
            Column {
                DetailText(title = "Used", text = rule.used.toString())
                DetailText(title = "Points", text = "${rule.winPoints}/${rule.maxPoints} +/- ${rule.pointsDifference}")
                DetailText(title = "Rest", text = "G:${rule.gamePointsForRest} / S:${rule.setPointsForRest} / P:${rule.pointPointsForRest}")
            }
        }
    }
}