package com.olt.racketclash.ui.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Tournament
import com.olt.racketclash.database.api.TournamentFilter
import com.olt.racketclash.database.api.TournamentSorting
import com.olt.racketclash.state.datetime.toFormattedString
import com.olt.racketclash.state.tournament.TournamentModel
import com.olt.racketclash.state.tournament.TournamentTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.*
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.TableSortDirection
import com.olt.racketclash.ui.layout.DetailSectionRow
import com.olt.racketclash.ui.layout.FilteredTable
import com.olt.racketclash.ui.layout.RacketClashDetailScaffold
import com.olt.racketclash.ui.layout.RacketClashTableScaffold

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

    RacketClashDetailScaffold(
        title = "Tournament",
        model = model,
        headerContent = {
            DetailSectionRow(title = state.item.name) {}
        },
        editOverlayContent = {
            AddOrUpdateTournamentOverlay(
                state = state.updatedItem,
                update = model::setUpdatedItem,
                locationSuggestions = state.data.locationSuggestions,
                onGetLocationSuggestions = model::locationSuggestions
            )
        }
    ) {
        TeamPreview(
            isLoading = state.isLoading,
            teams = state.data.teams,
            navigateTo = navigateTo
        )

        CategoryPreview(
            isLoading = state.isLoading,
            categories = state.data.categories,
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

    RacketClashTableScaffold(
        title = "Tournaments",
        model = model,
        filterOverlayContent = {
            FilterTournamentOverlay(
                state = state.filterUpdate,
                update = model::setFilter
            )
        },
        addOverlayContent = {
        AddOrUpdateTournamentOverlay(
                state = state.addItem,
                update = model::setNewItem,
                locationSuggestions = locationSuggestions,
                onGetLocationSuggestions = model::locationSuggestions
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
private fun FilterTournamentOverlay(
    state: TournamentFilter,
    update: (TournamentFilter) -> Unit
) {
    FormTextField(value = state.name, label = "Name") { update(state.copy(name = it)) }
    FormTextField(value = state.location, label = "Location") { update(state.copy(name = it)) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOrUpdateTournamentOverlay(
    state: Tournament,
    update: (Tournament) -> Unit,
    locationSuggestions: List<String>,
    onGetLocationSuggestions: (String) -> Unit,
) {
    FormTextField(
        value = state.name,
        label = "Name",
        isError = state.name.isBlank()
    ) { update(state.copy(name = it)) }

    FormRow {
        FormDropDownTextField(
            text = state.location,
            label = "Location",
            onTextChange = {
                update(state.copy(location = it))
                onGetLocationSuggestions(it)
            },
            dropDownItems = locationSuggestions,
            dropDownItemText = { Text(it) },
            onItemClicked = { update(state.copy(location = it)) }
        )

        FormNumberSelector(
            value = state.numberOfCourts,
            label = "Courts",
            range = 1..Int.MAX_VALUE,
            onUp = { update(state.copy(numberOfCourts = it)) },
            onDown = { update(state.copy(numberOfCourts = it)) }
        )
    }

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
internal fun TournamentPreview(
    isLoading: Boolean,
    tournaments: List<Tournament>,
    navigateTo: (View) -> Unit
) {
    ListPreviewBox(
        name = "Tournaments",
        isLoading = isLoading,
        items = tournaments,
        onNavigateMore = { navigateTo(View.Tournaments) }
    ) {
        ListPreviewBoxLink(
            text = it.name,
            subText = "(${it.start.toFormattedString()} to ${it.end.toFormattedString()})"
        ) { navigateTo(View.Tournament(tournamentId = it.id)) }
    }
}