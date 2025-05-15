package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.category.CategoryModel
import com.olt.racketclash.state.category.CategoryState
import com.olt.racketclash.state.category.CategoryTableModel
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.AddOrUpdateFormOverlay
import com.olt.racketclash.ui.base.layout.FilterFormOverlay
import com.olt.racketclash.ui.base.layout.FormDropDownTextField
import com.olt.racketclash.ui.base.layout.FormTextField
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.SimpleIconButton
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
    var showEditOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Categories",
        headerContent = { CategoryInfo(isLoading = state.isLoading, category = state.category) },
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            ) { showEditOverlay = !showEditOverlay }
        },
        overlay = {
            AddOrUpdateCategoryOverlay(
                visible = showEditOverlay,
                category = state.category,
                onConfirm = model::updatePlayer
            ) { showEditOverlay = false }
        }
    ) {
        CategoryBody(
            state = state,
            navigateTo = navigateTo
        )
    }
}

@Composable
internal fun Categories(
    database: Database,
    tournamentId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { CategoryTableModel(database = database.categories, tournamentId = tournamentId) }
    val state by model.state.collectAsState()
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showAddOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Categories",
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Search,
                contentDescription = "Filter"
            ) {
                showAddOverlay = false
                showFilterOverlay = !showFilterOverlay
            }

            SimpleIconButton(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            ) {
                showFilterOverlay = false
                showAddOverlay = !showAddOverlay
            }
        },
        overlay = {
            FilterCategoryOverlay(
                visible = showFilterOverlay,
                filter = state.filter,
                applyFilter = model::filter
            ) { showFilterOverlay = false }

            AddOrUpdateCategoryOverlay(
                visible = showAddOverlay,
                onConfirm = model::add
            ) { showAddOverlay = false }
        }
    ) {
        CategoryTable(
            state = state,
            onSort = model::sort,
            onDelete = model::delete,
            onSelectPage = model::selectPage,
            onNavigateTo = navigateTo,
            onApplyFilter = model::filter
        )
    }
}

@Composable
private fun BoxScope.FilterCategoryOverlay(
    filter: CategoryFilter,
    applyFilter: (CategoryFilter) -> Unit,
    visible: Boolean,
    dismissOverlay: () -> Unit
) {
    FilterFormOverlay(
        filterState = filter,
        visible = visible,
        dismissOverlay = dismissOverlay,
        onFilter = applyFilter
    ) { state, update ->
        FormTextField(value = state.name, label = "Name") { update { copy(name = it) } }
    }
}

@Composable
private fun BoxScope.AddOrUpdateCategoryOverlay(
    visible: Boolean,
    category: Category? = null,
    onConfirm: (Category) -> Unit,
    dismissOverlay: () -> Unit
) {
    AddOrUpdateFormOverlay(
        defaultItemState = Category(),
        itemState = category,
        visible = visible,
        dismissOverlay = dismissOverlay,
        canConfirm = { it.name.isNotBlank() },
        onConfirm = onConfirm
    ) { state, update ->
        FormTextField(
            value = state.name,
            label = "Name",
            isError = state.name.isBlank(),
            onValueChange = { update { copy(name = it) } }
        )

        FormDropDownTextField(
            text = state.type.text(),
            label = "Type",
            readOnly = true,
            dropDownItems = listOf(CategoryType.Custom, CategoryType.Table, CategoryType.Tree),
            dropDownItemText = { Text(it.text()) },
            onItemClicked = { update { copy(type = it) } }
        )
    }
}

@Composable
private fun CategoryTable(
    state: ListState<Category, CategoryFilter, CategorySorting>,
    onSort: (CategorySorting) -> Unit,
    onDelete: (Category) -> Unit,
    onSelectPage: (Int) -> Unit,
    onNavigateTo: (View) -> Unit,
    onApplyFilter: (CategoryFilter) -> Unit
) {
    FilteredTable(
        state = state,
        columns = columns(
            navigateTo = onNavigateTo,
            onSort = onSort,
            onDelete = onDelete
        ),
        onPageClicked = onSelectPage
    ) {
        if (it.name.isNotBlank())
            FilterChip(name = "Name", text = it.name) { onApplyFilter(it.copy(name = "")) }
    }
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
private fun CategoryInfo(
    isLoading: Boolean,
    category: Category
) {
    Details(
        isLoading = isLoading,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        DetailSectionRow(title = category.name) {
            DetailText(title = "id", text = category.id.toString())
        }
    }
}

@Composable
private fun CategoryBody(
    state: CategoryState,
    navigateTo: (View) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {

    }
}

private fun CategoryType.text(): String =
    when (this) {
        CategoryType.Custom -> "Custom"
        CategoryType.Table -> "List"
        CategoryType.Tree -> "Tree"
    }