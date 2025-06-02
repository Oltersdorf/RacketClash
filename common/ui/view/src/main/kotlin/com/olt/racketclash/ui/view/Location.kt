package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Location
import com.olt.racketclash.database.api.LocationFilter
import com.olt.racketclash.database.api.LocationSorting
import com.olt.racketclash.state.location.LocationModel
import com.olt.racketclash.state.location.LocationTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.FormRow
import com.olt.racketclash.ui.base.layout.FormTextField
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.TableSortDirection
import com.olt.racketclash.ui.layout.*

@Composable
internal fun Location(
    database: Database,
    locationId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember {
        LocationModel(
            locationDatabase = database.locations,
            tournamentDatabase = database.tournaments,
            locationId = locationId
        )
    }
    val state by model.state.collectAsState()

    RacketClashDetailScaffold(
        title = "Location",
        model = model,
        headerContent = {
            DetailSectionRow(title = state.item.name) {
                Column {
                    DetailText(title = "Address", text = state.item.address)
                    DetailText(title = "Postcode", text = state.item.postcode)
                    DetailText(title = "City", text = state.item.city)
                }

                Column {
                    DetailText(title = "Country", text = state.item.country)
                    DetailText(title = "GPS-Coordinates", text = state.item.gpsCoordinates)
                    DetailText(title = "Google Maps", text = state.item.gpsCoordinates)
                }
            }
        },
        editOverlayContent = {
            AddOrUpdateLocationOverlay(
                state = state.updatedItem,
                update = model::setUpdatedItem
            )
        }
    ) {
        TournamentPreview(
            isLoading = state.isLoading,
            tournaments = state.data.tournaments,
            navigateTo = navigateTo
        )
    }
}

@Composable
internal fun Locations(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { LocationTableModel(database = database.locations) }
    val state by model.state.collectAsState()

    RacketClashTableScaffold(
        title = "Locations",
        model = model,
        filterOverlayContent = {
            FilterLocationOverlay(
                state = state.filterUpdate,
                update = model::setFilter
            )
        },
        addOverlayContent = {
            AddOrUpdateLocationOverlay(
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
            if (it.address.isNotBlank())
                FilterChip(name = "Address", text = it.address) { model.setAndApplyFilter(it.copy(address = "")) }
            if (it.postcode.isNotBlank())
                FilterChip(name = "Postcode", text = it.postcode) { model.setAndApplyFilter(it.copy(postcode = "")) }
            if (it.city.isNotBlank())
                FilterChip(name = "City", text = it.city) { model.setAndApplyFilter(it.copy(city = "")) }
            if (it.country.isNotBlank())
                FilterChip(name = "Country", text = it.country) { model.setAndApplyFilter(it.copy(country = "")) }
        }
    }
}

@Composable
private fun FilterLocationOverlay(
    state: LocationFilter,
    update: (LocationFilter) -> Unit
) {
    FormTextField(
        value = state.name,
        label = "Name",
        isError = state.name.isBlank(),
        onValueChange = { update(state.copy(name = it)) }
    )

    FormRow {
        FormTextField(
            value = state.address,
            label = "Address",
            isError = state.address.isBlank(),
            onValueChange = { update(state.copy(address = it)) }
        )

        FormTextField(
            value = state.postcode,
            label = "Postcode",
            isError = state.postcode.isBlank(),
            onValueChange = { update(state.copy(postcode = it)) }
        )

        FormTextField(
            value = state.city,
            label = "City",
            isError = state.city.isBlank(),
            onValueChange = { update(state.copy(city = it)) }
        )
    }

    FormTextField(
        value = state.country,
        label = "Country",
        isError = state.country.isBlank(),
        onValueChange = { update(state.copy(country = it)) }
    )
}

@Composable
private fun AddOrUpdateLocationOverlay(
    state: Location,
    update: (Location) -> Unit
) {
    FormTextField(
        value = state.name,
        label = "Name",
        isError = state.name.isBlank(),
        onValueChange = { update(state.copy(name = it)) }
    )

    FormRow {
        FormTextField(
            value = state.address,
            label = "Address",
            isError = state.address.isBlank(),
            onValueChange = { update(state.copy(address = it)) }
        )

        FormTextField(
            value = state.postcode,
            label = "Postcode",
            isError = state.postcode.isBlank(),
            onValueChange = { update(state.copy(postcode = it)) }
        )

        FormTextField(
            value = state.city,
            label = "City",
            isError = state.city.isBlank(),
            onValueChange = { update(state.copy(city = it)) }
        )
    }

    FormRow {
        FormTextField(
            value = state.country,
            label = "Country",
            isError = state.country.isBlank(),
            onValueChange = { update(state.copy(country = it)) }
        )

        FormTextField(
            value = state.gpsCoordinates,
            label = "GPS-Coordinates",
            isError = state.gpsCoordinates.isBlank(),
            onValueChange = { update(state.copy(gpsCoordinates = it)) }
        )
    }
}

private fun columns(
    navigateTo: (View) -> Unit,
    onSort: (LocationSorting) -> Unit,
    onDelete: (Location) -> Unit
): List<LazyTableColumn<Location>> =
    listOf(
        LazyTableColumn.Link(name = "Name", text = { it.name }, weight = 0.2f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(LocationSorting.NameAsc)
                TableSortDirection.Descending -> onSort(LocationSorting.NameDesc)
            }
        }) {
            navigateTo(View.Location(locationId = it.id))
        },
        LazyTableColumn.Text(name = "Address", weight = 0.2f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(LocationSorting.AddressAsc)
                TableSortDirection.Descending -> onSort(LocationSorting.AddressDesc)
            }
        }) { it.address },
        LazyTableColumn.Text(name = "Postcode", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(LocationSorting.PostcodeAsc)
                TableSortDirection.Descending -> onSort(LocationSorting.PostcodeDesc)
            }
        }) { it.postcode },
        LazyTableColumn.Text(name = "City", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(LocationSorting.CityAsc)
                TableSortDirection.Descending -> onSort(LocationSorting.CityDesc)
            }
        }) { it.city },
        LazyTableColumn.Text(name = "Country", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(LocationSorting.CountryAsc)
                TableSortDirection.Descending -> onSort(LocationSorting.CountryDesc)
            }
        }) { it.country },
        LazyTableColumn.Text(name = "GPS-Coordinates", weight = 0.2f) { it.gpsCoordinates },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = onDelete,
            enabled = { false },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )