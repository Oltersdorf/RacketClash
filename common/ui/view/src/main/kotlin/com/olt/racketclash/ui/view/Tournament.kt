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
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Tournament
import com.olt.racketclash.database.api.TournamentFilter
import com.olt.racketclash.database.api.TournamentSorting
import com.olt.racketclash.state.datetime.toFormattedString
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.state.tournament.TournamentModel
import com.olt.racketclash.state.tournament.TournamentState
import com.olt.racketclash.state.tournament.TournamentTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.*
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.SimpleIconButton
import com.olt.racketclash.ui.base.material.TableSortDirection
import com.olt.racketclash.ui.layout.DetailSectionRow
import com.olt.racketclash.ui.layout.Details
import com.olt.racketclash.ui.layout.FilteredTable
import com.olt.racketclash.ui.layout.RacketClashScaffold
import java.time.Instant

@Composable
internal fun Tournament(
    database: Database,
    tournamentId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember {
        TournamentModel(
            tournamentDatabase = database.tournaments,
            playerDatabase = database.players,
            teamDatabase = database.teams,
            categoryDatabase = database.categories,
            gameDatabase = database.games,
            scheduleDatabase = database.schedules,
            tournamentId = tournamentId
        )
    }
    val state by model.state.collectAsState()
    var showEditOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Tournament",
        headerContent = { TournamentInfo(isLoading = state.isLoading, tournament = state.tournament) },
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            ) { showEditOverlay = !showEditOverlay }
        },
        overlay = {
            AddOrUpdateTournamentOverlay(
                visible = showEditOverlay,
                tournament = state.tournament,
                locationSuggestions = state.locationSuggestions,
                onGetLocationSuggestions = model::locationSuggestions,
                onConfirm = model::updateTournament
            ) { showEditOverlay = false }
        }
    ) {
        TournamentBody(
            state = state,
            navigateTo = navigateTo
        )
    }
}

@Composable
internal fun Tournaments(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { TournamentTableModel(database = database.tournaments) }
    val state by model.state.collectAsState()
    val locationSuggestions by model.locationSuggestionsState.collectAsState()
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showAddOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Tournaments",
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
            FilterTournamentOverlay(
                filter = state.filter,
                applyFilter = model::filter,
                visible = showFilterOverlay
            ) { showFilterOverlay = false }

            AddOrUpdateTournamentOverlay(
                visible = showAddOverlay,
                locationSuggestions = locationSuggestions,
                onGetLocationSuggestions = model::locationSuggestions,
                onConfirm = model::add
            ) { showAddOverlay = false }
        }
    ) {
        TournamentTable(
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
private fun BoxScope.FilterTournamentOverlay(
    filter: TournamentFilter,
    applyFilter: (TournamentFilter) -> Unit,
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
        FormTextField(value = state.location, label = "Location") { update { copy(name = it) } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.AddOrUpdateTournamentOverlay(
    visible: Boolean,
    tournament: Tournament? = null,
    locationSuggestions: List<String>,
    onGetLocationSuggestions: (String) -> Unit,
    onConfirm: (Tournament) -> Unit,
    dismissOverlay: () -> Unit
) {
    val dateRange = rememberDateRangePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedStartDateMillis = tournament?.start?.toEpochMilli(),
        initialSelectedEndDateMillis = tournament?.end?.toEpochMilli()
    )

    AddOrUpdateFormOverlay(
        defaultItemState = Tournament(),
        itemState = tournament,
        visible = visible,
        dismissOverlay = dismissOverlay,
        canConfirm = { it.name.isNotBlank() },
        onConfirm = {
            onConfirm(
                it.copy(
                    start = Instant.ofEpochMilli(dateRange.selectedStartDateMillis ?: Instant.EPOCH.toEpochMilli()),
                    end = Instant.ofEpochMilli(dateRange.selectedEndDateMillis ?: Instant.EPOCH.toEpochMilli())
                )
            )
        }
    ) { state, update ->
        FormTextField(
            value = state.name,
            label = "Name",
            isError = state.name.isBlank()
        ) { update { copy(name = it) } }

        FormRow {
            FormDropDownTextField(
                text = state.location,
                label = "Location",
                onTextChange = {
                    update { copy(location = it) }
                    onGetLocationSuggestions(it)
                },
                dropDownItems = locationSuggestions,
                dropDownItemText = { Text(it) },
                onItemClicked = { update { copy(location = it) } }
            )

            FormNumberSelector(
                value = state.numberOfCourts,
                label = "Courts",
                range = 1..Int.MAX_VALUE,
                onUp = { update { copy(numberOfCourts = it) } },
                onDown = { update { copy(numberOfCourts = it) } }
            )
        }

        FormDateRangePicker(state = dateRange)

        /*FormRow {
            FormDropDownTextField(
                text = "${startTime.hour}:${startTime.minute}",
                label = "Start time",
                readOnly = true,
                dropDownItems = state.suggestedTimes,
                dropDownItemText = { Text(it) },
                onItemClicked = { update { copy(start = ) } }
            )

            FormDropDownTextField(
                text = "${endTime.hour}:${endTime.minute}",
                label = "End time",
                readOnly = true,
                dropDownItems = state.suggestedTimes,
                dropDownItemText = { Text(it) },
                onItemClicked = model::updateTimeEnd
            )
        }*/
    }
}

@Composable
private fun TournamentTable(
    state: ListState<Tournament, TournamentFilter, TournamentSorting>,
    onSort: (TournamentSorting) -> Unit,
    onDelete: (Tournament) -> Unit,
    onSelectPage: (Int) -> Unit,
    onNavigateTo: (View) -> Unit,
    onApplyFilter: (TournamentFilter) -> Unit
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
    onSort: (TournamentSorting) -> Unit,
    onDelete: (Tournament) -> Unit
): List<LazyTableColumn<Tournament>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.2f, text = { it.name }, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TournamentSorting.NameAsc)
                TableSortDirection.Descending -> onSort(TournamentSorting.NameDesc)
            }
        }) { navigateTo(View.Tournament(tournamentId = it.id)) },
        LazyTableColumn.Text(name = "Location", weight = 0.2f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TournamentSorting.LocationAsc)
                TableSortDirection.Descending -> onSort(TournamentSorting.LocationDesc)
            }
        }) { it.location },
        LazyTableColumn.Text(name = "Courts", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TournamentSorting.CourtsAsc)
                TableSortDirection.Descending -> onSort(TournamentSorting.CourtsDesc)
            }
        }) { it.numberOfCourts.toString() },
        LazyTableColumn.Text(name = "Start", weight = 0.15f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TournamentSorting.StartAsc)
                TableSortDirection.Descending -> onSort(TournamentSorting.StartDesc)
            }
        }) { it.start.toFormattedString() },
        LazyTableColumn.Text(name = "End", weight = 0.15f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TournamentSorting.EndAsc)
                TableSortDirection.Descending -> onSort(TournamentSorting.EndDesc)
            }
        }) { it.end.toFormattedString() },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TournamentSorting.PlayersAsc)
                TableSortDirection.Descending -> onSort(TournamentSorting.PlayersDesc)
            }
        }) { it.playersCount.toString() },
        LazyTableColumn.Text(name = "Categories", weight = 0.1f) { it.categoriesCount.toString() },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = { onDelete(it) },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )

@Composable
private fun TournamentInfo(
    isLoading: Boolean,
    tournament: Tournament
) {
    Details(
        isLoading = isLoading,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        DetailSectionRow(title = tournament.name) {

        }
    }
}

@Composable
private fun TournamentBody(
    state: TournamentState,
    navigateTo: (View) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {
        ListPreviewBox(
            name = "Teams",
            isLoading = false,
            items = state.teams,
            onNavigateMore = { navigateTo(View.Teams(tournamentId = state.tournament.id)) }
        ) {
            ListPreviewBoxLink(
                text = it.name,
                subText = "(Rank: ${it.rank}, Size: ${it.size})"
            ) { navigateTo(View.Team(teamId = it.id)) }
        }

        ListPreviewBox(
            name = "Categories",
            isLoading = false,
            items = state.categories,
            onNavigateMore = { navigateTo(View.Categories(tournamentId = state.tournament.id)) }
        ) {
            ListPreviewBoxLink(
                text = it.name,
                subText = "(Type: ${it.type})"
            ) { navigateTo(View.Category(categoryId = it.id)) }
        }

        ListPreviewBox(
            name = "Schedule",
            isLoading = false,
            items = state.scheduledGames,
            onNavigateMore = { navigateTo(View.Schedule(tournamentId = state.tournament.id)) }
        ) {
            ListPreviewBoxLink(
                text = "Schedule",
                subText = ""
            ) { navigateTo(View.Schedule(tournamentId = it.tournamentId)) }
        }
    }
}