package com.olt.racketclash.ui.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.category.CategoryModel
import com.olt.racketclash.state.category.CategoryTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.FormDropDownTextField
import com.olt.racketclash.ui.base.layout.FormTextField
import com.olt.racketclash.ui.base.layout.ListPreviewBox
import com.olt.racketclash.ui.base.layout.ListPreviewBoxLink
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.TableSortDirection
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.material.Status

@Composable
internal fun Category(
    database: Database,
    categoryId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember {
        CategoryModel(
            categoryDatabase = database.categories,
            categoryId = categoryId
        )
    }
    val state by model.state.collectAsState()

    RacketClashDetailScaffold(
        title = "Categories",
        model = model,
        headerContent = {
            DetailSectionRow(title = state.item.name) {
                DetailText(title = "id", text = state.item.id.toString())
            }
        },
        editOverlayContent = {
            AddOrUpdateCategoryOverlay(
                state = state.updatedItem,
                update = model::setUpdatedItem
            )
        }
    ) {}
}

@Composable
internal fun Categories(
    database: Database,
    tournamentId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { CategoryTableModel(database = database.categories, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    RacketClashTableScaffold(
        title = "Categories",
        model = model,
        filterOverlayContent = {
            FilterCategoryOverlay(
                state = state.filterUpdate,
                update = model::setFilter
            )
        },
        addOverlayContent = {
            AddOrUpdateCategoryOverlay(
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
private fun FilterCategoryOverlay(
    state: CategoryFilter,
    update: (CategoryFilter) -> Unit
) {
    FormTextField(value = state.name, label = "Name") { update(state.copy(name = it)) }
}

@Composable
private fun AddOrUpdateCategoryOverlay(
    state: Category,
    update: (Category) -> Unit
) {
    FormTextField(
        value = state.name,
        label = "Name",
        isError = state.name.isBlank(),
        onValueChange = { update(state.copy(name = it)) }
    )

    FormDropDownTextField(
        text = state.type.text(),
        label = "Type",
        readOnly = true,
        dropDownItems = listOf(CategoryType.Custom, CategoryType.Table, CategoryType.Tree),
        dropDownItemText = { Text(it.text()) },
        onItemClicked = { update(state.copy(type = it)) }
    )
}

private fun columns(
    navigateTo: (View) -> Unit,
    onSort: (CategorySorting) -> Unit,
    onDelete: (Category) -> Unit
): List<LazyTableColumn<Category>> =
    listOf(
        LazyTableColumn.Builder(name = "Status", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(CategorySorting.StatusAsc)
                TableSortDirection.Descending -> onSort(CategorySorting.StatusDesc)
            }
        }) { category, weight ->
            Status(modifier = Modifier.weight(weight), isOkay = category.finished)
        },
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(CategorySorting.NameAsc)
                TableSortDirection.Descending -> onSort(CategorySorting.NameDesc)
            }
        }) { navigateTo(View.Category(categoryId = it.id)) },
        LazyTableColumn.Text(name = "Type", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(CategorySorting.TypeAsc)
                TableSortDirection.Descending -> onSort(CategorySorting.TypeDesc)
            }
        }) { it.type.text() },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(CategorySorting.PlayersAsc)
                TableSortDirection.Descending -> onSort(CategorySorting.PlayersDesc)
            }
        }) { it.players.toString() },

        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = { onDelete(it) },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )

@Composable
internal fun CategoryPreview(
    isLoading: Boolean,
    categories: List<Category>,
    navigateTo: (View) -> Unit
) {
    ListPreviewBox(
        name = "Categories",
        isLoading = isLoading,
        items = categories,
        onNavigateMore = { navigateTo(View.Tournaments) }
    ) {
        ListPreviewBoxLink(
            text = it.name,
            subText = "(${it.tournamentName})"
        ) { navigateTo(View.Tournament(tournamentId = it.id)) }
    }
}

private fun CategoryType.text(): String =
    when (this) {
        CategoryType.Custom -> "Custom"
        CategoryType.Table -> "List"
        CategoryType.Tree -> "Tree"
    }