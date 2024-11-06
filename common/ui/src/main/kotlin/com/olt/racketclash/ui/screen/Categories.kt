package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Status
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

private data class Category(
    val id: Long = 0L,
    val name: String = "Test Category",
    val players: Int = 24,
    val finished: Boolean = false
)

private sealed class TagTypeCategory {
    data class Name(val text: String) : TagTypeCategory()
    data object Finished : TagTypeCategory()
    data object NotFinished : TagTypeCategory()
}

@Composable
internal fun Categories(
    database: Database,
    tournamentId: Long,
    navigateTo: (Screens) -> Unit
) {
    var searchBarText by remember { mutableStateOf("1") }
    var availableTags by remember { mutableStateOf( listOf(
        TagTypeCategory.Name("1"),
        TagTypeCategory.Finished,
        TagTypeCategory.NotFinished
    )) }
    var tags by remember { mutableStateOf(listOf(
        TagTypeCategory.Name("1"),
        TagTypeCategory.Finished,
        TagTypeCategory.NotFinished
    )) }
    var categories by remember { mutableStateOf(listOf(
        Category(),
        Category(finished = true)
    )) }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(2) }
    var isLoading by remember { mutableStateOf(false) }

    SearchableLazyTableWithScroll(
        title = "Categories",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateCategory(categoryName = null, categoryId = null, tournamentId = tournamentId)) },
        items = categories,
        isLoading = isLoading,
        columns = columns(
            navigateTo = navigateTo,
            tournamentId = tournamentId,
            onNameSortAscending = {},
            onNameSortDescending = {},
            onPlayersSortAscending = {},
            onPlayersSortDescending = {},
            onStatusSortAscending = {},
            onStatusSortDescending = {}
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
    tournamentId: Long,
    onNameSortAscending: () -> Unit,
    onNameSortDescending: () -> Unit,
    onPlayersSortAscending: () -> Unit,
    onPlayersSortDescending: () -> Unit,
    onStatusSortAscending: () -> Unit,
    onStatusSortDescending: () -> Unit
): List<LazyTableColumn<Category>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSortAscending()
                LazyTableSortDirection.Descending -> onNameSortDescending()
            }
        }) { navigateTo(Screens.Category(categoryName = it.name, categoryId = it.id, tournamentId = tournamentId)) },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onPlayersSortAscending()
                LazyTableSortDirection.Descending -> onPlayersSortDescending()
            }
        }) { it.players.toString() },
        LazyTableColumn.Builder(name = "Status", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onStatusSortAscending()
                LazyTableSortDirection.Descending -> onStatusSortDescending()
            }
        }) { category, weight ->
            Status(modifier = Modifier.weight(weight), isOkay = category.finished)
        }
    )

@Composable
private fun TagText(tagType: TagTypeCategory) =
    when (tagType) {
        is TagTypeCategory.Name -> Tag(name = "Name", text = tagType.text)
        TagTypeCategory.Finished -> Tag(name = "Finished")
        TagTypeCategory.NotFinished -> Tag(name = "Not finished")
    }