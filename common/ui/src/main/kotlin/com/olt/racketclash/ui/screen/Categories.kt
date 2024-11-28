package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.olt.racketclash.categories.CategoriesModel
import com.olt.racketclash.database.Database
import com.olt.racketclash.database.category.CategoryType
import com.olt.racketclash.database.category.DeletableCategory
import com.olt.racketclash.database.category.Sorting
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.SearchBarMenuItem
import com.olt.racketclash.ui.component.SearchBarTagChip
import com.olt.racketclash.ui.component.Status
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

@OptIn(ExperimentalLayoutApi::class)
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
            onSort = model::onSort,
            onDelete = model::deleteCategory
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
                state.availableTags.finished?.let {
                    SearchBarMenuItem(name = "Finished") { model.addFinishedTag(value = true) }
                    SearchBarMenuItem(name = "Not finished") { model.addFinishedTag(value = false) }
                }
            }
        ) {
            state.tags.name?.let {
                SearchBarTagChip(name = "Name", text = it, onRemove = model::removeNameTag)
            }
            state.tags.finished?.let {
                if (it)
                    SearchBarTagChip(name = "Finished", onRemove = model::removeFinishedTag)
                else
                    SearchBarTagChip("Not finished", onRemove = model::removeFinishedTag)
            }
        }
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    tournamentId: Long,
    onSort: (Sorting) -> Unit,
    onDelete: (Long) -> Unit
): List<LazyTableColumn<DeletableCategory>> =
    listOf(
        LazyTableColumn.Builder(name = "Status", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.StatusAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.StatusDesc)
            }
        }) { category, weight ->
            Status(modifier = Modifier.weight(weight), isOkay = category.finished)
        },
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.NameDesc)
            }
        }) { navigateTo(Screens.Category(categoryName = it.name, categoryId = it.id, tournamentId = tournamentId)) },
        LazyTableColumn.Text(name = "Type", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.TypeAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.TypeDesc)
            }
        }) { it.type.text() },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.PlayersAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.PlayersDesc)
            }
        }) { it.players.toString() },

        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = { onDelete(it.id) },
            enabled = { it.deletable },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )

private fun CategoryType.text(): String =
    when (this) {
        CategoryType.Custom -> "Custom"
        CategoryType.List -> "List"
        CategoryType.Tree -> "Tree"
    }