package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.rackeclash.rules.Rule
import com.olt.rackeclash.rules.RulesModel
import com.olt.rackeclash.rules.Tag
import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

@Composable
internal fun Rules(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    val model = RulesModel(database = database)
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Rules",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateRule(ruleName = null, ruleId = null)) },
        items = state.rules,
        isLoading = state.isLoading,
        columns = columns(
            navigateTo = navigateTo,
            onNameSort = model::onNameSort
        ),
        currentPage = state.currentPage,
        lastPage = state.lastPage,
        onPageClicked = model::goToPage
    ) {
        var searchBarText by remember { mutableStateOf("") }

        SearchBar(
            text = searchBarText,
            onTextChange = {
                searchBarText = it
                model.updateAvailableTags(text = it)
            },
            dropDownItems = state.availableTags,
            onDropDownItemClick = model::addTag,
            tags = state.tags,
            onTagRemove = model::removeTag,
            tagText = { TagText(it) }
        )
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    onNameSort: (SortDirection) -> Unit
): List<LazyTableColumn<Rule>> =
    listOf(
        LazyTableColumn.Link(name = "Name", text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onNameSort(SortDirection.Descending)
            }
        }) {
            navigateTo(Screens.AddOrUpdateRule(ruleName = it.name, ruleId = it.id))
        },
        LazyTableColumn.Text(name = "Sets") { "${it.winSets}/${it.maxSets}" },
        LazyTableColumn.Text(name = "Points") { "${it.winPoints}/${it.maxPoints} +/- ${it.pointsDifference}" },
        LazyTableColumn.Text(name = "Rating") { "W:${it.gamePointsForWin} / D:${it.gamePointsForDraw} / L:${it.gamePointsForLose}" },
        LazyTableColumn.Text(name = "Rest") { "G:${it.gamePointsForRest} / S:${it.setPointsForRest} / P:${it.pointPointsForRest}" }
    )

@Composable
private fun TagText(tagType: Tag) =
    when (tagType) {
        is Tag.Name -> Tag(name = "Name", text = tagType.text)
    }
