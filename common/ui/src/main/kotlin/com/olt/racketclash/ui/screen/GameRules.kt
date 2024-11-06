package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

private data class GameRule(
    val id: Long = 0L,
    val name: String = "Test",
    val maxSets: Int = 3,
    val winSets: Int = 2,
    val maxPoints: Int = 30,
    val winPoints: Int = 21,
    val pointsDifference: Int = 2,
    val gamePointsForWin: Int = 2,
    val gamePointsForLose: Int = 0,
    val gamePointsForDraw: Int = 1,
    val gamePointsForRest: Int = 2,
    val setPointsForRest: Int = 0,
    val pointPointsForRest: Int = 0
)

private sealed class TagTypeGameRule {
    data class Name(val text: String) : TagTypeGameRule()
}

@Composable
internal fun GameRules(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    //TODD: replace with database
    var gameRules by remember { mutableStateOf(listOf(GameRule())) }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var searchBarText by remember { mutableStateOf("") }
    var availableTags by remember { mutableStateOf(listOf<TagTypeGameRule>(TagTypeGameRule.Name("A"))) }
    var tags by remember { mutableStateOf(listOf<TagTypeGameRule>(TagTypeGameRule.Name("A"))) }

    SearchableLazyTableWithScroll(
        title = "Game rules",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateGameRule(gameRuleName = null, gameRuleId = null)) },
        items = gameRules,
        isLoading = isLoading,
        columns = columns(
            navigateTo = navigateTo,
            onNameSortAscending = {},
            onNameSortDescending = {}
        ),
        currentPage = currentPage,
        lastPage = lastPage,
        onPageClicked = { currentPage = it }
    ) {
        SearchBar(
            text = searchBarText,
            onTextChange = { searchBarText = it },
            dropDownItems = availableTags,
            onDropDownItemClick = { tags += it },
            tags = tags,
            onTagRemove = { tags -= it },
            tagText = { TagText(it) }
        )
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    onNameSortAscending: () -> Unit,
    onNameSortDescending: () -> Unit
): List<LazyTableColumn<GameRule>> =
    listOf(
        LazyTableColumn.Link(name = "Name", text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSortAscending()
                LazyTableSortDirection.Descending -> onNameSortDescending()
            }
        }) {
            navigateTo(Screens.AddOrUpdateGameRule(gameRuleName = it.name, gameRuleId = it.id))
        },
        LazyTableColumn.Text(name = "Sets") { "${it.winSets}/${it.maxSets}" },
        LazyTableColumn.Text(name = "Points") { "${it.winPoints}/${it.maxPoints} +/- ${it.pointsDifference}" },
        LazyTableColumn.Text(name = "Rating") { "W:${it.gamePointsForWin} / D:${it.gamePointsForDraw} / L:${it.gamePointsForLose}" },
        LazyTableColumn.Text(name = "Rest") { "G:${it.gamePointsForRest} / S:${it.setPointsForRest} / P:${it.pointPointsForRest}" }
    )

@Composable
private fun TagText(tagType: TagTypeGameRule) =
    when (tagType) {
        is TagTypeGameRule.Name -> Tag(name = "Name", text = tagType.text)
    }
