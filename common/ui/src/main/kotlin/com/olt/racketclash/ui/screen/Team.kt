package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.RatioBar
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.navigate.Screens

private data class TeamPlayer(
    val id: Long = 0L,
    val name: String = "Test Player",
    val birthYear: Int = 1900,
    val club: String = "Test club",
    val winRatioSingle: Triple<Int, Int, Int> = Triple(10, 0, 20),
    val winRatioDouble: Triple<Int, Int, Int> = Triple(15, 0, 12)
)

private sealed class TagTypeTeamPlayer {
    data class Name(val text: String) : TagTypeTeamPlayer()
    data class BirthYear(val text: String) : TagTypeTeamPlayer()
    data class Club(val text: String) : TagTypeTeamPlayer()
}

@Composable
internal fun Team(
    database: Database,
    teamId: Long,
    teamName: String,
    tournamentId: Long,
    navigateTo: (Screens) -> Unit
) {
    val isLoading by remember { mutableStateOf(false) }
    val players = remember { 15 }
    val doubleGamePoints = remember { Triple(15, 1, 20) }
    val doubleSetPoints = remember { Triple(15, 1, 20) }
    val doublePointPoints = remember { Triple(15, 1, 20) }
    val singleGamePoints = remember { Triple(15, 1, 20) }
    val singleSetPoints = remember { Triple(15, 1, 20) }
    val singlePointPoints = remember { Triple(15, 1, 20) }
    var searchBar by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(2) }
    var teamPlayers by remember { mutableStateOf(listOf(
        TeamPlayer(),
        TeamPlayer(winRatioSingle = Triple(1, 0, 100)),
        TeamPlayer(winRatioSingle = Triple(0, 0, 5)),
        TeamPlayer(winRatioSingle = Triple(10, 10, 23)),
        TeamPlayer(winRatioSingle = Triple(10, 10, 100))
    )) }
    var availableTags by remember { mutableStateOf(listOf(
        TagTypeTeamPlayer.Name("1"),
        TagTypeTeamPlayer.BirthYear("1"),
        TagTypeTeamPlayer.Club("1")
    )) }
    var tags by remember { mutableStateOf(listOf(
        TagTypeTeamPlayer.Name("1"),
        TagTypeTeamPlayer.BirthYear("1"),
        TagTypeTeamPlayer.Club("1")
    )) }

    Details(
        isLoading = isLoading,
        onEdit = { navigateTo(Screens.AddOrUpdateTeam(teamId = teamId, teamName = teamName, tournamentId = tournamentId)) }
    ) {
        DetailSection(title = "Description") {
            DetailText(title = "Name", text = "$teamName ($players Players)")
        }

        DetailSection(title = "Statistics") {
            StatisticsDetail(
                doubleGamePoints = doubleGamePoints,
                doubleSetPoints = doubleSetPoints,
                doublePointPoints = doublePointPoints,
                singleGamePoints = singleGamePoints,
                singleSetPoints = singleSetPoints,
                singlePointPoints = singlePointPoints
            )
        }

        DetailSection(title = "Players") {
            SearchableLazyTableWithScroll(
                items = teamPlayers,
                columns = columns(
                    navigateTo = navigateTo,
                    onNameSortAscending = {},
                    onNameSortDescending = {},
                    onBirthYearSortAscending = {},
                    onBirthYearSortDescending = {},
                    onClubSortAscending = {},
                    onClubSortDescending = {},
                    onSinglesSortAscending = {},
                    onSinglesSortDescending = {},
                    onDoublesSortAscending = {},
                    onDoublesSortDescending = {}
                ),
                currentPage = currentPage,
                lastPage = lastPage,
                onPageClicked = { currentPage = it }
            ) {
                SearchBar(
                    text = searchBar,
                    onTextChange = { searchBar = it },
                    dropDownItems = availableTags,
                    onDropDownItemClick = { tags += it },
                    tags = tags,
                    onTagRemove = { tags -= it },
                    tagText = { TagText(it) }
                )
            }
        }
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    onNameSortAscending: () -> Unit,
    onNameSortDescending: () -> Unit,
    onBirthYearSortAscending: () -> Unit,
    onBirthYearSortDescending: () -> Unit,
    onClubSortAscending: () -> Unit,
    onClubSortDescending: () -> Unit,
    onSinglesSortAscending: () -> Unit,
    onSinglesSortDescending: () -> Unit,
    onDoublesSortAscending: () -> Unit,
    onDoublesSortDescending: () -> Unit
): List<LazyTableColumn<TeamPlayer>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.4f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSortAscending()
                LazyTableSortDirection.Descending -> onNameSortDescending()
            }
        }) { navigateTo(Screens.Player(playerName = it.name, playerId = it.id)) },
        LazyTableColumn.Text(name = "Birth year", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onBirthYearSortAscending()
                LazyTableSortDirection.Descending -> onBirthYearSortDescending()
            }
        }) { it.birthYear.toString() },
        LazyTableColumn.Text(name = "Club", weight = 0.4f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onClubSortAscending()
                LazyTableSortDirection.Descending -> onClubSortDescending()
            }
        }) { it.club },
        LazyTableColumn.Builder(name = "Single", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSinglesSortAscending()
                LazyTableSortDirection.Descending -> onSinglesSortDescending()
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
                LazyTableSortDirection.Ascending -> onDoublesSortAscending()
                LazyTableSortDirection.Descending -> onDoublesSortDescending()
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
private fun TagText(tagType: TagTypeTeamPlayer) =
    when (tagType) {
        is TagTypeTeamPlayer.BirthYear -> Tag(name = "Birth year", text = tagType.text)
        is TagTypeTeamPlayer.Club -> Tag(name = "Club", text = tagType.text)
        is TagTypeTeamPlayer.Name -> Tag(name = "Name", text = tagType.text)
    }