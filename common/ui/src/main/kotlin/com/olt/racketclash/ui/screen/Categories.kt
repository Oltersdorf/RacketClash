package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.olt.racketclash.categories.CategoriesModel
import com.olt.racketclash.categories.Category
import com.olt.racketclash.categories.Tag
import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Status
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

@Composable
internal fun Categories(
    database: Database,
    tournamentId: Long,
    navigateTo: (Screens) -> Unit
) {
    val model = remember { CategoriesModel(database = database, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Categories",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateCategory(categoryName = null, categoryId = null, tournamentId = tournamentId)) },
        items = state.categories,
        isLoading = state.isLoading,
        columns = columns(
            navigateTo = navigateTo,
            tournamentId = tournamentId,
            onNameSort = model::onNameSort,
            onPlayersSort = model::onPlayerSort,
            onStatusSort = model::onStatusSort,
        ),
        currentPage = state.currentPage,
        lastPage = state.lastPage,
        onPageClicked = model::updatePage
    ) {
        SearchBar(
            text = state.searchBarText,
            onTextChange = model::updateSearchBar,
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
    tournamentId: Long,
    onNameSort: (SortDirection) -> Unit,
    onPlayersSort: (SortDirection) -> Unit,
    onStatusSort: (SortDirection) -> Unit
): List<LazyTableColumn<Category>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onNameSort(SortDirection.Descending)
            }
        }) { navigateTo(Screens.Category(categoryName = it.name, categoryId = it.id, tournamentId = tournamentId)) },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onPlayersSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onPlayersSort(SortDirection.Descending)
            }
        }) { it.players.toString() },
        LazyTableColumn.Builder(name = "Status", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onStatusSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onStatusSort(SortDirection.Descending)
            }
        }) { category, weight ->
            Status(modifier = Modifier.weight(weight), isOkay = category.finished)
        }
    )

@Composable
private fun TagText(tagType: Tag) =
    when (tagType) {
        is Tag.Name -> Tag(name = "Name", text = tagType.text)
        Tag.Finished -> Tag(name = "Finished")
        Tag.NotFinished -> Tag(name = "Not finished")
    }