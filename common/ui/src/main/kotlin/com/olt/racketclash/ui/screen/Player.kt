package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.Link
import com.olt.racketclash.ui.component.RatioBar
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.navigate.Screens

private data class PlayerGame(
    val id: Long = 0L,
    val date: String = "01/01/2024 15:00",
    val playerLeftOneId: Long = 0L,
    val playerLeftOneName: String = "Test player",
    val playerLeftTwoId: Long? = 0L,
    val playerLeftTwoName: String? = "Test player",
    val playerRightOneId: Long = 0L,
    val playerRightOneName: String = "Test player",
    val playerRightTwoId: Long? = 0L,
    val playerRightTwoName: String? = "Test player",
    val results: List<Pair<Int, Int>> = listOf(21 to 18, 22 to 20),
    val tournamentId: Long = 0L,
    val tournamentName: String = "Test tournament",
    val categoryId: Long = 0L,
    val categoryName: String = "Test category",
    val gameRuleId: Long = 0L,
    val gameRuleName: String = "Test game rule",
    val totalGamePoints: Pair<Int, Int> = 2 to 0,
    val totalSetPoints: Pair<Int, Int> = 2 to 0,
    val totalPointPoints: Pair<Int, Int> = 43 to 38
)

private sealed class TagTypePlayerGame {
    data class Date(val text: String) : TagTypePlayerGame()
    data class Tournament(val text: String) : TagTypePlayerGame()
    data class Category(val text: String) : TagTypePlayerGame()
    data class GameRule(val text: String) : TagTypePlayerGame()
    data class Player(val text: String) : TagTypePlayerGame()
}

private data class PlayerTournament(
    val id: Long = 0L,
    val name: String = "Test tournament",
    val teamId: Long? = 0L,
    val teamName: String? = "Test team",
    val categories: List<PlayerTournamentCategory> = listOf(PlayerTournamentCategory(), PlayerTournamentCategory())
)

private data class PlayerTournamentCategory(
    val id: Long = 0L,
    val name: String = "Test Category",
    val rank: String = "1/24"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Player(
    database: Database,
    playerId: Long,
    playerName: String,
    navigateTo: (Screens) -> Unit
) {
    val isLoading by remember { mutableStateOf(false) }
    val birthYear = remember { 1900 }
    val club = remember { "Test club" }
    val firstGame = remember { "01/01/2024" }
    val lastGame = remember { "02/01/2024" }
    val doubleGamePoints = remember { Triple(15, 1, 20) }
    val doubleSetPoints = remember { Triple(15, 1, 20) }
    val doublePointPoints = remember { Triple(15, 1, 20) }
    val singleGamePoints = remember { Triple(15, 1, 20) }
    val singleSetPoints = remember { Triple(15, 1, 20) }
    val singlePointPoints = remember { Triple(15, 1, 20) }
    var searchBar by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(2) }
    var playerGames by remember { mutableStateOf(listOf(
        PlayerGame(),
        PlayerGame(),
        PlayerGame(),
        PlayerGame(),
        PlayerGame()
    )) }
    var availableTags by remember { mutableStateOf(listOf(
        TagTypePlayerGame.Date("01/01/2024"),
        TagTypePlayerGame.Tournament("Test tournament"),
        TagTypePlayerGame.Category("Test category"),
        TagTypePlayerGame.GameRule("Test game rule"),
        TagTypePlayerGame.Player("Test player")
    )) }
    var tags by remember { mutableStateOf(listOf(
        TagTypePlayerGame.Date("01/01/2024"),
        TagTypePlayerGame.Tournament("Test tournament"),
        TagTypePlayerGame.Category("Test category"),
        TagTypePlayerGame.GameRule("Test game rule"),
        TagTypePlayerGame.Player("Test player")
    )) }
    val tournaments = remember { listOf(
        PlayerTournament(),
        PlayerTournament()
    ) }

    Details(
        isLoading = isLoading,
        onEdit = { navigateTo(Screens.AddOrUpdatePlayer(playerName = playerName, playerId = playerId)) }
    ) {
        DetailSection(title = "Description") {
            DetailText(title = "Name", text = "$playerName (Year: $birthYear)")
            DetailText(title = "Club", text = club)
        }

        DetailSection(title = "Statistics") {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                DetailText(title = "First game", text = firstGame)
                DetailText(title = "Last game", text = lastGame)
            }

            StatisticsDetail(
                doubleGamePoints = doubleGamePoints,
                doubleSetPoints = doubleSetPoints,
                doublePointPoints = doublePointPoints,
                singleGamePoints = singleGamePoints,
                singleSetPoints = singleSetPoints,
                singlePointPoints = singlePointPoints
            )
        }

        DetailSection("Tournaments") {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                tournaments.forEach { tournament ->
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(tournament.name)
                        if (tournament.teamId != null && tournament.teamName != null)
                            Link(tournament.teamName) {
                                navigateTo(Screens.Team(
                                    tournamentId = tournament.id,
                                    teamId = tournament.teamId,
                                    teamName = tournament.teamName
                                ))
                            }

                        if (tournament.categories.isNotEmpty()) {
                            Text("(")

                            FlowRow {
                                tournament.categories.forEachIndexed { index, category ->
                                    Link(category.name) {
                                        navigateTo(Screens.Category(
                                            tournamentId = tournament.id,
                                            categoryId = category.id,
                                            categoryName = category.name
                                        ))
                                    }
                                    Text(": ${category.rank}${ if (index + 1 < tournament.categories.size) ", " else "" }")
                                }
                            }

                            Text(")")
                        }
                    }
                }
            }
        }

        DetailSection(title = "Games") {
            SearchableLazyTableWithScroll(
                items = playerGames,
                columns = columns(
                    navigateTo = navigateTo,
                    onDateSortAscending = {},
                    onDateSortDescending = {},
                    onTournamentSortAscending = {},
                    onTournamentSortDescending = {},
                    onCategorySortAscending = {},
                    onCategorySortDescending = {},
                    playerId = playerId
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
    onDateSortAscending: () -> Unit,
    onDateSortDescending: () -> Unit,
    onTournamentSortAscending: () -> Unit,
    onTournamentSortDescending: () -> Unit,
    onCategorySortAscending: () -> Unit,
    onCategorySortDescending: () -> Unit,
    playerId: Long
): List<LazyTableColumn<PlayerGame>> =
    listOf(
        LazyTableColumn.Text(name = "Date", weight = 0.08f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onDateSortAscending()
                LazyTableSortDirection.Descending -> onDateSortDescending()
            }
        }) { it.date },
        LazyTableColumn.Builder(name = "Tournament", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onTournamentSortAscending()
                LazyTableSortDirection.Descending -> onTournamentSortDescending()
            }
        }) { game, weight ->
            Row(
                modifier = Modifier.weight(weight),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(game.tournamentName)
                Link("(${game.categoryName})") {
                    Screens.Category(
                        tournamentId = game.tournamentId,
                        categoryId = game.categoryId,
                        categoryName = game.categoryName
                    )
                }
            }
        },
        LazyTableColumn.Text(name = "Game Rule", weight = 0.08f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onCategorySortAscending()
                LazyTableSortDirection.Descending -> onCategorySortDescending()
            }
        }) { it.gameRuleName },
        LazyTableColumn.Builder(name = "Left", weight = 0.1f) { game, weight ->
            Column(modifier = Modifier.weight(weight)) {
                if (game.playerLeftOneId == playerId)
                    Text(game.playerLeftOneName)
                else
                    Link(game.playerLeftOneName) {
                        navigateTo(Screens.Player(playerId = game.playerLeftOneId, playerName = game.playerLeftOneName))
                    }

                if (game.playerLeftTwoId != null && game.playerLeftTwoName != null) {
                    if (game.playerLeftTwoId == playerId)
                        Text(game.playerLeftTwoName)
                    else
                        Link(game.playerLeftTwoName) {
                            navigateTo(Screens.Player(playerId = game.playerLeftTwoId, playerName = game.playerLeftTwoName))
                        }
                }
            }
        },
        LazyTableColumn.Builder(name = "Results", weight = 0.05f) { game, weight ->
            val playerIsLeft = game.playerLeftOneId == playerId || game.playerLeftTwoId == playerId

            Column(modifier = Modifier.weight(weight)) {
                game.results.forEach {
                    Row {
                        Text(it.first.toString(), fontWeight = if (playerIsLeft) FontWeight.Bold else null)
                        Text(":")
                        Text(it.second.toString(), fontWeight = if (!playerIsLeft) FontWeight.Bold else null)
                    }
                }
            }
        },
        LazyTableColumn.Builder(name = "Right", weight = 0.1f) { game, weight ->
            Column(modifier = Modifier.weight(weight)) {
                if (game.playerRightOneId == playerId)
                    Text(game.playerRightOneName)
                else
                    Link(game.playerRightOneName) {
                        navigateTo(Screens.Player(playerId = game.playerRightOneId, playerName = game.playerRightOneName))
                    }

                if (game.playerRightTwoId != null && game.playerRightTwoName != null) {
                    if (game.playerRightTwoId == playerId)
                        Text(game.playerRightTwoName)
                    else
                        Link(game.playerRightTwoName) {
                            navigateTo(Screens.Player(playerId = game.playerRightTwoId, playerName = game.playerRightTwoName))
                        }
                }
            }
        },
        LazyTableColumn.Builder(name = "Game", weight = 0.05f) { game, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = game.totalGamePoints.first,
                right = game.totalGamePoints.second
            )
        },
        LazyTableColumn.Builder(name = "Sets", weight = 0.05f) { game, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = game.totalSetPoints.first,
                right = game.totalSetPoints.second
            )
        },
        LazyTableColumn.Builder(name = "Points", weight = 0.05f) { game, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = game.totalPointPoints.first,
                right = game.totalPointPoints.second
            )
        }
    )

@Composable
private fun TagText(tagType: TagTypePlayerGame) =
    when (tagType) {
        is TagTypePlayerGame.Date -> Tag(name = "Date", text = tagType.text)
        is TagTypePlayerGame.Tournament -> Tag(name = "Tournament", text = tagType.text)
        is TagTypePlayerGame.Category -> Tag(name = "Category", text = tagType.text)
        is TagTypePlayerGame.GameRule -> Tag(name = "Game rule", text = tagType.text)
        is TagTypePlayerGame.Player -> Tag(name = "Player", text = tagType.text)
    }