package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.team.Player
import com.olt.racketclash.team.Tag
import com.olt.racketclash.team.TeamModel
import com.olt.racketclash.ui.component.RatioBar
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.navigate.Screens

@Composable
internal fun Team(
    database: Database,
    teamId: Long,
    teamName: String,
    tournamentId: Long,
    navigateTo: (Screens) -> Unit
) {
    val model = remember { TeamModel(database = database, teamId = teamId, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    Details(
        isLoading = state.isLoading,
        onEdit = { navigateTo(Screens.AddOrUpdateTeam(teamId = teamId, teamName = teamName, tournamentId = tournamentId)) }
    ) {
        DetailSection(title = "Description") {
            DetailText(title = "Name", text = "$teamName (${state.numberOfPlayers} Players)")
        }

        DetailSection(title = "Statistics") {
            StatisticsDetail(
                doubleGamePoints = state.doubleGamePoints,
                doubleSetPoints = state.doubleSetPoints,
                doublePointPoints = state.doublePointPoints,
                singleGamePoints = state.singleGamePoints,
                singleSetPoints = state.singleSetPoints,
                singlePointPoints = state.singlePointPoints
            )
        }

        DetailSection(title = "Players") {
            SearchableLazyTableWithScroll(
                items = state.players,
                columns = columns(
                    navigateTo = navigateTo,
                    onNameSort = model::onNameSort,
                    onBirthYearSort = model::onBirthYearSort,
                    onClubSort = model::onClubSort,
                    onSinglesSort = model::onSinglesSort,
                    onDoublesSort = model::onDoublesSort
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
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    onNameSort: (SortDirection) -> Unit,
    onBirthYearSort: (SortDirection) -> Unit,
    onClubSort: (SortDirection) -> Unit,
    onSinglesSort: (SortDirection) -> Unit,
    onDoublesSort: (SortDirection) -> Unit
): List<LazyTableColumn<Player>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.4f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onNameSort(SortDirection.Descending)
            }
        }) { navigateTo(Screens.Player(playerName = it.name, playerId = it.id)) },
        LazyTableColumn.Text(name = "Birth year", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onBirthYearSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onBirthYearSort(SortDirection.Descending)
            }
        }) { it.birthYear.toString() },
        LazyTableColumn.Text(name = "Club", weight = 0.4f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onClubSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onClubSort(SortDirection.Descending)
            }
        }) { it.club },
        LazyTableColumn.Builder(name = "Single", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSinglesSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onSinglesSort(SortDirection.Descending)
            }
        }) { player, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioSingle.first,
                middle = player.winRatioSingle.second,
                right = player.winRatioSingle.third
            )
        },
        LazyTableColumn.Builder(name = "Double", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onDoublesSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onDoublesSort(SortDirection.Descending)
            }
        }) { player, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioDouble.first,
                middle = player.winRatioDouble.second,
                right = player.winRatioDouble.third
            )
        }
    )

@Composable
private fun TagText(tagType: Tag) =
    when (tagType) {
        is Tag.BirthYear -> Tag(name = "Birth year", text = tagType.text)
        is Tag.Club -> Tag(name = "Club", text = tagType.text)
        is Tag.Name -> Tag(name = "Name", text = tagType.text)
    }