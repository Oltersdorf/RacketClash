package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.tournaments.Tag
import com.olt.racketclash.tournaments.Tournament
import com.olt.racketclash.tournaments.TournamentsModel
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

@Composable
internal fun Tournaments(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    val model = remember { TournamentsModel(database = database) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Tournaments",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateTournament(tournamentName = null, tournamentId = null)) },
        items = state.tournaments,
        isLoading = state.isLoading,
        columns = columns(
            navigateTo = navigateTo,
            onNameSort = model::onNameSort,
            onLocationSort = model::onLocationSort,
            onCourtsSort = model::onCourtsSort,
            onStartDateTimeSort = model::onStartDateTimeSort,
            onEndDateTimeSort = model::onEndDateTimeSort,
            onPlayersSort = model::onPlayersSort
        ),
        currentPage = state.currentPage,
        lastPage = state.lastPage,
        onPageClicked = model::updatePage
    ) {
        SearchBar(
            text = state.searchBarText,
            onTextChange = model::updateSearchBar,
            dropDownItems = state.availableTags,
            onDropDownItemClick = model::addTag,
            tags = state.tags,
            onTagRemove = model::removeTag,
            tagText = { TagText(it) }
        )
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    onNameSort: (SortDirection) -> Unit,
    onLocationSort: (SortDirection) -> Unit,
    onCourtsSort: (SortDirection) -> Unit,
    onStartDateTimeSort: (SortDirection) -> Unit,
    onEndDateTimeSort: (SortDirection) -> Unit,
    onPlayersSort: (SortDirection) -> Unit
): List<LazyTableColumn<Tournament>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.25f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onNameSort(SortDirection.Descending)
            }
        }) { navigateTo(Screens.Tournament(tournamentName = it.name, tournamentId = it.id)) },
        LazyTableColumn.Text(name = "Location", weight = 0.25f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onLocationSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onLocationSort(SortDirection.Descending)
            }
        }) { it.location },
        LazyTableColumn.Text(name = "Courts", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onCourtsSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onCourtsSort(SortDirection.Descending)
            }
        }) { it.numberOfCourts.toString() },
        LazyTableColumn.Text(name = "Start", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onStartDateTimeSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onStartDateTimeSort(SortDirection.Descending)
            }
        }) { it.startDateTime },
        LazyTableColumn.Text(name = "End", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onEndDateTimeSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onEndDateTimeSort(SortDirection.Descending)
            }
        }) { it.endDateTime },
        LazyTableColumn.Text(name = "Players", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onPlayersSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onPlayersSort(SortDirection.Descending)
            }
        }) { it.playersCount.toString() },
        LazyTableColumn.Text(name = "Categories", weight = 0.1f) { it.categoriesCount.toString() }
    )

@Composable
private fun TagText(tagType: Tag) =
    when (tagType) {
        is Tag.Year -> Tag(name = "Date", text = tagType.text)
        is Tag.Location -> Tag(name = "Location", text = tagType.text)
        is Tag.Name -> Tag(name = "Name", text = tagType.text)
    }