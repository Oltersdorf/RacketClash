package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Rule
import com.olt.racketclash.database.api.RuleFilter
import com.olt.racketclash.database.api.RuleSorting
import com.olt.racketclash.state.rule.RuleModel
import com.olt.racketclash.state.rule.RuleTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.FormNumberSelector
import com.olt.racketclash.ui.base.layout.FormRow
import com.olt.racketclash.ui.base.layout.FormTextField
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.TableSortDirection
import com.olt.racketclash.ui.layout.*
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
internal fun Rule(
    database: Database,
    ruleId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember {
        RuleModel(
            ruleDatabase = database.rules,
            tournamentDatabase = database.tournaments,
            categoryDatabase = database.categories,
            gameDatabase = database.games,
            ruleId = ruleId
        )
    }
    val state by model.state.collectAsState()

    RacketClashDetailScaffold(
        title = "Rule",
        model = model,
        headerContent = {
            DetailSectionRow(title = state.item.name) {
                Column {
                    DetailText(title = "id", text = state.item.id.toString())
                    DetailText(title = "Sets", text = "${state.item.winSets}/${state.item.maxSets}")
                    DetailText(title = "Rating", text = "W:${state.item.gamePointsForWin} / D:${state.item.gamePointsForDraw} / L:${state.item.gamePointsForLose}")
                }
                Column {
                    DetailText(title = "Used", text = state.item.used.toString())
                    DetailText(title = "Points", text = "${state.item.winPoints}/${state.item.maxPoints} +/- ${state.item.pointsDifference}")
                    DetailText(title = "Rest", text = "G:${state.item.gamePointsForRest} / S:${state.item.setPointsForRest} / P:${state.item.pointPointsForRest}")
                }
            }
        },
        editOverlayContent = {
            AddOrUpdateRuleOverlay(
                state = state.updatedItem,
                update = model::setUpdatedItem
            )
        }
    ) {
        TournamentPreview(
            isLoading = state.isLoading,
            tournaments = state.data.tournaments,
            navigateTo = navigateTo
        )

        CategoryPreview(
            isLoading = state.isLoading,
            categories = state.data.categories,
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

    RacketClashTableScaffold(
        title = "Rules",
        model = model,
        filterOverlayContent = {
            FilterRuleOverlay(
                state = state.filterUpdate,
                update = model::setFilter
            )
        },
        addOverlayContent = {
            AddOrUpdateRuleOverlay(
                state = state.addItem,
                update = model::setNewItem
            )
        }
    ) {
        FilteredTable(
            state = state,
            columns = columns(
                navigateTo = navigateTo,
                onSort = model::sort,
                onDelete = model::delete
            ),
            onPageClicked = model::selectPage
        ) {
            if (it.name.isNotBlank())
                FilterChip(name = "Name", text = it.name) { model.setAndApplyFilter(it.copy(name = "")) }
        }
    }
}

@Composable
private fun FilterRuleOverlay(
    state: RuleFilter,
    update: (RuleFilter) -> Unit
) {
    FormTextField(value = state.name, label = "Name") { update(state.copy(name = it)) }
}

@Composable
private fun AddOrUpdateRuleOverlay(
    state: Rule,
    update: (Rule) -> Unit
) {
    FormTextField(
        value = state.name,
        label = "Name",
        isError = state.name.isBlank(),
        onValueChange = { update(state.copy(name = it)) }
    )

    FormRow {
        FormNumberSelector(
            value = state.maxSets,
            label = "Max sets",
            range = 1..Int.MAX_VALUE,
            onUp = {
                update(
                    state.copy(
                        maxSets = it,
                        winSets = max(state.winSets, ceil(it.toDouble() / 2).roundToInt())
                    )
                )
            },
            onDown = {
                update(
                    state.copy(
                        maxSets = it,
                        winSets = min(it, state.winSets)
                    )
                )
            }
        )

        FormNumberSelector(
            value = state.winSets,
            label = "Win sets",
            range = 1..Int.MAX_VALUE,
            onUp = {
                update(
                    state.copy(
                        winSets = it,
                        maxSets = max(state.maxSets, it)
                    )
                )
            },
            onDown = {
                update(
                    state.copy(
                        winSets = it,
                        maxSets = min(state.maxSets, it * 2)
                    )
                )
            }
        )
    }

    FormRow {
        FormNumberSelector(
            value = state.maxPoints,
            label = "Max points",
            range = 1..Int.MAX_VALUE,
            onUp = { update(state.copy(maxPoints = it)) },
            onDown = {
                update(
                    state.copy(
                        maxPoints = it,
                        winPoints = min(state.winPoints, it),
                        pointsDifference = min(state.pointsDifference, it)
                    )
                )
            }
        )

        FormNumberSelector(
            value = state.winPoints,
            label = "Win points",
            range = 1..Int.MAX_VALUE,
            onUp = {
                update(
                    state.copy(
                        winPoints = it,
                        maxPoints = max(state.maxPoints, it)
                    )
                )
            },
            onDown = {
                update(
                    state.copy(
                        winPoints = it,
                        pointsDifference = min(state.pointsDifference, it)
                    )
                )
            }
        )

        FormNumberSelector(
            value = state.pointsDifference,
            label = "Points difference",
            range = 0..Int.MAX_VALUE,
            onUp = {
                update(
                    state.copy(
                        pointsDifference = it,
                        winPoints = max(state.winPoints, it),
                        maxPoints = max(state.maxPoints, it)
                    )
                )
            },
            onDown = { update(state.copy(pointsDifference = it)) }
        )
    }

    FormRow {
        FormNumberSelector(
            value = state.gamePointsForWin,
            label = "Game points for win",
            range = 0..Int.MAX_VALUE,
            onUp = { update(state.copy(gamePointsForWin = it)) },
            onDown = { update(state.copy(gamePointsForWin = it)) }
        )

        FormNumberSelector(
            value = state.gamePointsForLose,
            label = "Game points for lose",
            range = 0..Int.MAX_VALUE,
            onUp = { update(state.copy(gamePointsForLose = it)) },
            onDown = { update(state.copy(gamePointsForLose = it)) }
        )

        FormNumberSelector(
            value = state.gamePointsForDraw,
            label = "Game points for draw",
            range = 0..Int.MAX_VALUE,
            onUp = { update(state.copy(gamePointsForDraw = it)) },
            onDown = { update(state.copy(gamePointsForDraw = it)) }
        )
    }

    FormRow {
        FormNumberSelector(
            value = state.gamePointsForRest,
            label = "Game points for rest",
            range = 0..Int.MAX_VALUE,
            onUp = { update(state.copy(gamePointsForRest = it)) },
            onDown = { update(state.copy(gamePointsForRest = it)) }
        )

        FormNumberSelector(
            value = state.setPointsForRest,
            label = "Set points for rest",
            range = 0..Int.MAX_VALUE,
            onUp = { update(state.copy(setPointsForRest = it)) },
            onDown = { update(state.copy(setPointsForRest = it)) }
        )

        FormNumberSelector(
            value = state.pointPointsForRest,
            label = "Point points for rest",
            range = 0..Int.MAX_VALUE,
            onUp = { update(state.copy(pointPointsForRest = it)) },
            onDown = { update(state.copy(pointPointsForRest = it)) }
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
                TableSortDirection.Ascending -> onSort(RuleSorting.NameAsc)
                TableSortDirection.Descending -> onSort(RuleSorting.NameDesc)
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