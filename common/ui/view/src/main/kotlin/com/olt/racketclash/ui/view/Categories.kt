package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.olt.racketclash.categories.CategoriesModel
import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.CategorySorting
import com.olt.racketclash.database.api.CategoryType
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.ui.base.material.SearchBar
import com.olt.racketclash.ui.base.material.SearchBarMenuItem
import com.olt.racketclash.ui.base.material.SearchBarTagChip
import com.olt.racketclash.ui.material.Status
import com.olt.racketclash.ui.base.layout.LazyTableColumn
import com.olt.racketclash.ui.base.layout.LazyTableSortDirection
import com.olt.racketclash.ui.base.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.View

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Categories(
    database: Database,
    tournamentId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { CategoriesModel(database = database.categories, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Categories",
        onTitleAdd = { navigateTo(View.AddOrUpdateCategory(categoryName = null, categoryId = null, tournamentId = tournamentId)) },
        items = state.categories,
        isLoading = state.isLoading,
        columns = columns(
            navigateTo = navigateTo,
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
    navigateTo: (View) -> Unit,
    onSort: (CategorySorting) -> Unit,
    onDelete: (Long) -> Unit
): List<LazyTableColumn<Category>> =
    listOf(
        LazyTableColumn.Builder(name = "Status", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(CategorySorting.StatusAsc)
                LazyTableSortDirection.Descending -> onSort(CategorySorting.StatusDesc)
            }
        }) { category, weight ->
            Status(modifier = Modifier.weight(weight), isOkay = category.finished)
        },
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(CategorySorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(CategorySorting.NameDesc)
            }
        }) {
            navigateTo(
                View.Category(
                categoryName = it.name,
                categoryId = it.id,
                tournamentId = it.tournamentId
            ))
        },
        LazyTableColumn.Text(name = "Type", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(CategorySorting.TypeAsc)
                LazyTableSortDirection.Descending -> onSort(CategorySorting.TypeDesc)
            }
        }) { it.type.text() },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(CategorySorting.PlayersAsc)
                LazyTableSortDirection.Descending -> onSort(CategorySorting.PlayersDesc)
            }
        }) { it.players.toString() },

        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = { onDelete(it.id) },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )

private fun CategoryType.text(): String =
    when (this) {
        CategoryType.Custom -> "Custom"
        CategoryType.Table -> "List"
        CategoryType.Tree -> "Tree"
    }