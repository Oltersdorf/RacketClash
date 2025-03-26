package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Tournament
import com.olt.racketclash.database.api.TournamentSorting
import com.olt.racketclash.tournaments.TournamentsModel
import com.olt.racketclash.ui.base.material.SearchBar
import com.olt.racketclash.ui.base.material.SearchBarMenuItem
import com.olt.racketclash.ui.base.material.SearchBarTagChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.LazyTableSortDirection
import com.olt.racketclash.ui.base.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.View
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Tournaments(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { TournamentsModel(database = database.tournaments) }
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
    onSort: (TournamentSorting) -> Unit,
    onDelete: (Long) -> Unit
): List<LazyTableColumn<Tournament>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.2f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TournamentSorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(TournamentSorting.NameDesc)
            }
        }) { navigateTo(View.Tournament(tournamentName = it.name, tournamentId = it.id)) },
        LazyTableColumn.Text(name = "Location", weight = 0.2f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TournamentSorting.LocationAsc)
                LazyTableSortDirection.Descending -> onSort(TournamentSorting.LocationDesc)
            }
        }) { it.location },
        LazyTableColumn.Text(name = "Courts", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TournamentSorting.CourtsAsc)
                LazyTableSortDirection.Descending -> onSort(TournamentSorting.CourtsDesc)
            }
        }) { it.numberOfCourts.toString() },
        LazyTableColumn.Text(name = "Start", weight = 0.15f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TournamentSorting.StartAsc)
                LazyTableSortDirection.Descending -> onSort(TournamentSorting.StartDesc)
            }
        }) {
            LocalDateTime
                .ofInstant(it.start, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm z"))
        },
        LazyTableColumn.Text(name = "End", weight = 0.15f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TournamentSorting.EndAsc)
                LazyTableSortDirection.Descending -> onSort(TournamentSorting.EndDesc)
            }
        }) {
            LocalDateTime
                .ofInstant(it.end, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm z"))
        },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TournamentSorting.PlayersAsc)
                LazyTableSortDirection.Descending -> onSort(TournamentSorting.PlayersDesc)
            }
        }) { it.playersCount.toString() },
        LazyTableColumn.Text(name = "Categories", weight = 0.1f) { it.categoriesCount.toString() },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = { onDelete(it.id) },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )