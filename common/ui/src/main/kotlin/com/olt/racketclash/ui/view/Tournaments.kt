package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.database.tournament.DeletableTournament
import com.olt.racketclash.database.tournament.Sorting
import com.olt.racketclash.tournaments.TournamentsModel
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.SearchBarMenuItem
import com.olt.racketclash.ui.component.SearchBarTagChip
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.View

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Tournaments(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { TournamentsModel(database = database) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Tournaments",
        onTitleAdd = { navigateTo(View.AddOrUpdateTournament(tournamentName = null, tournamentId = null)) },
        items = state.tournaments,
        isLoading = state.isLoading,
        columns = columns(
            navigateTo = navigateTo,
            onSort = model::onSort,
            onDelete = model::deleteTournament
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
                state.availableTags.location?.let {
                    SearchBarMenuItem(name = "Location", text = it, onClick = model::addLocationTag)
                }
                state.availableTags.year?.let {
                    SearchBarMenuItem(name = "Year", text = it.toString(), onClick = model::addYearTag)
                }
            }
        ) {
            state.availableTags.name?.let {
                SearchBarTagChip(name = "Name", text = it, onRemove = model::removeNameTag)
            }
            state.availableTags.location?.let {
                SearchBarTagChip(name = "Location", text = it, onRemove = model::removeLocationTag)
            }
            state.availableTags.year?.let {
                SearchBarTagChip(name = "Year", text = it.toString(), onRemove = model::removeYearTag)
            }
        }
    }
}

private fun columns(
    navigateTo: (View) -> Unit,
    onSort: (Sorting) -> Unit,
    onDelete: (Long) -> Unit
): List<LazyTableColumn<DeletableTournament>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.2f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.NameDesc)
            }
        }) { navigateTo(View.Tournament(tournamentName = it.name, tournamentId = it.id)) },
        LazyTableColumn.Text(name = "Location", weight = 0.2f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.LocationAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.LocationDesc)
            }
        }) { it.location },
        LazyTableColumn.Text(name = "Courts", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.CourtsAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.CourtsDesc)
            }
        }) { it.numberOfCourts.toString() },
        LazyTableColumn.Text(name = "Start", weight = 0.15f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.StartDateTimeAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.StartDateTimeDesc)
            }
        }) { it.startDateTime },
        LazyTableColumn.Text(name = "End", weight = 0.15f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.EndDateTimeAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.EndDateTimeDesc)
            }
        }) { it.endDateTime },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.PlayersAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.PlayersDesc)
            }
        }) { it.playersCount.toString() },
        LazyTableColumn.Text(name = "Categories", weight = 0.1f) { it.categoriesCount.toString() },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = { onDelete(it.id) },
            enabled = { it.deletable },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )