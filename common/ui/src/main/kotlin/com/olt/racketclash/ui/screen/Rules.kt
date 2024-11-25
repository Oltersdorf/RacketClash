package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import com.olt.rackeclash.rules.RulesModel
import com.olt.racketclash.database.rule.Sorting
import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.DeletableRule
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.SearchBarMenuItem
import com.olt.racketclash.ui.component.SearchBarTagChip
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Rules(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    val model = remember { RulesModel(database = database) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Rules",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateRule(ruleName = null, ruleId = null)) },
        items = state.rules,
        isLoading = state.isLoading,
        columns = columns(
            navigateTo = navigateTo,
            onSort = model::onSort,
            onDelete = model::deleteRule
        ),
        currentPage = state.currentPage,
        lastPage = state.lastPage,
        onPageClicked = model::updatePage
    ) {
        SearchBar(
            text = state.searchBarText,
            onTextChange = model::updateSearchBar,
            dropDownItems = {
                state.availableTags.name?.let {
                    SearchBarMenuItem(name = "Name", text = it, onClick = model::addNameTag)
                }
            }
        ) {
            state.tags.name?.let {
                SearchBarTagChip(name = "Name", text = it, onRemove = model::removeNameTag)
            }
        }
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    onSort: (Sorting) -> Unit,
    onDelete: (Long) -> Unit
): List<LazyTableColumn<DeletableRule>> =
    listOf(
        LazyTableColumn.Link(name = "Name", text = { it.name }, weight = 0.6f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.NameDesc)
            }
        }) {
            navigateTo(Screens.AddOrUpdateRule(ruleName = it.name, ruleId = it.id))
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
            onClick = { onDelete(it.id) },
            enabled = { it.deletable },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )
