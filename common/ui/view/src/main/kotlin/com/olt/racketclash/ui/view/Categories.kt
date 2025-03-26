package com.olt.racketclash.ui.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.olt.racketclash.categories.CategoriesModel
import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.ui.material.Status
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.LazyTableSortDirection
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.layout.FilteredLazyTable

@Composable
internal fun Categories(
    database: Database,
    tournamentId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { CategoriesModel(database = database.categories, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    FilteredLazyTable(
        state = ListState(
            isLoading = state.isLoading,
            items = state.categories,
            filter = CategoryFilter(),
            sorting = CategorySorting.NameAsc
        ),
        columns = columns(
            navigateTo = navigateTo,
            onSort = model::onSort,
            onDelete = model::deleteCategory
        ),
        onPageClicked = model::updatePage
    ) {

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