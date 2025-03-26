package com.olt.racketclash.ui.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Tournament
import com.olt.racketclash.database.api.TournamentFilter
import com.olt.racketclash.database.api.TournamentSorting
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.tournaments.TournamentsModel
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.LazyTableSortDirection
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.layout.FilteredLazyTable
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun Tournaments(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { TournamentsModel(database = database.tournaments) }
    val state by model.state.collectAsState()

    FilteredLazyTable(
        state = ListState(
            isLoading = state.isLoading,
            items = state.tournaments,
            filter = TournamentFilter(),
            sorting = TournamentSorting.NameAsc
        ),
        columns = columns(
            navigateTo = navigateTo,
            onSort = model::onSort,
            onDelete = model::deleteTournament
        ),
        onPageClicked = model::updatePage
    ) {

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