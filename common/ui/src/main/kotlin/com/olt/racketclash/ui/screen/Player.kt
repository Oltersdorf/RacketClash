package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.player.Game
import com.olt.racketclash.player.PlayerModel
import com.olt.racketclash.player.Tag
import com.olt.racketclash.ui.component.Link
import com.olt.racketclash.ui.component.RatioBar
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.Screens

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Player(
    database: Database,
    playerId: Long,
    playerName: String,
    navigateTo: (Screens) -> Unit
) {
    val model = remember { PlayerModel(database = database, playerId = playerId) }
    val state by model.state.collectAsState()

    Details(
        isLoading = state.isLoading,
        onEdit = { navigateTo(Screens.AddOrUpdatePlayer(playerName = playerName, playerId = playerId)) }
    ) {
        DetailSection(title = "Description") {
            DetailText(title = "Name", text = "$playerName (Year: ${state.birthYear})")
            DetailText(title = "Club", text = state.club)
        }

        DetailSection(title = "Statistics") {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                DetailText(title = "First game", text = state.firstGame)
                DetailText(title = "Last game", text = state.lastGame)
            }

            StatisticsDetail(
                doubleGamePoints = state.doubleGamePoints,
                doubleSetPoints = state.doubleSetPoints,
                doublePointPoints = state.doublePointPoints,
                singleGamePoints = state.singleGamePoints,
                singleSetPoints = state.singleSetPoints,
                singlePointPoints = state.singlePointPoints
            )
        }

        DetailSection("Tournaments") {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                state.tournaments.forEach { tournament ->
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(tournament.name)

                        val teamId = tournament.teamId
                        val teamName = tournament.teamName
                        if (teamId != null && teamName != null)
                            Link(teamName) {
                                navigateTo(
                                    Screens.Team(
                                    tournamentId = tournament.id,
                                    teamId = teamId,
                                    teamName = teamName
                                ))
                            }

                        if (tournament.categories.isNotEmpty()) {
                            Text("(")

                            FlowRow {
                                tournament.categories.forEachIndexed { index, category ->
                                    Link(category.name) {
                                        navigateTo(
                                            Screens.Category(
                                            categoryId = category.id,
                                            categoryName = category.name,
                                            tournamentId = tournament.id
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
                items = state.games,
                columns = columns(
                    navigateTo = navigateTo,
                    onDateSort = model::onDateSort,
                    onTournamentSort = model::onTournamentSort,
                    onCategorySort = model::onCategorySort,
                    playerId = playerId
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
    onDateSort: () -> Unit,
    onTournamentSort: () -> Unit,
    onCategorySort: () -> Unit,
    playerId: Long
): List<LazyTableColumn<Game>> =
    listOf(
        LazyTableColumn.Text(name = "Date", weight = 0.08f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onDateSort()
                LazyTableSortDirection.Descending -> onDateSort()
            }
        }) { it.date },
        LazyTableColumn.Builder(name = "Tournament", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onTournamentSort()
                LazyTableSortDirection.Descending -> onTournamentSort()
            }
        }) { game, weight ->
            Row(
                modifier = Modifier.weight(weight),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(game.tournamentName)
                Link("(${game.categoryName})") {
                    Screens.Category(
                        categoryId = game.categoryId,
                        categoryName = game.categoryName,
                        tournamentId = game.tournamentId
                    )
                }
            }
        },
        LazyTableColumn.Text(name = "Game Rule", weight = 0.08f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onCategorySort()
                LazyTableSortDirection.Descending -> onCategorySort()
            }
        }) { it.ruleName },
        LazyTableColumn.Builder(name = "Left", weight = 0.1f) { game, weight ->
            Column(modifier = Modifier.weight(weight)) {
                if (game.playerLeftOneId == playerId)
                    Text(game.playerLeftOneName)
                else
                    Link(game.playerLeftOneName) {
                        navigateTo(Screens.Player(playerId = game.playerLeftOneId, playerName = game.playerLeftOneName))
                    }

                val playerLeftTwoId = game.playerLeftTwoId
                val playerLeftTwoName = game.playerLeftTwoName
                if (playerLeftTwoId != null && playerLeftTwoName != null) {
                    if (playerLeftTwoId == playerId)
                        Text(playerLeftTwoName)
                    else
                        Link(playerLeftTwoName) {
                            navigateTo(Screens.Player(playerId = playerLeftTwoId, playerName = playerLeftTwoName))
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

                val playerRightTwoId = game.playerRightTwoId
                val playerRightTwoName = game.playerRightTwoName
                if (playerRightTwoId != null && playerRightTwoName != null) {
                    if (playerRightTwoId == playerId)
                        Text(playerRightTwoName)
                    else
                        Link(playerRightTwoName) {
                            navigateTo(Screens.Player(playerId = playerRightTwoId, playerName = playerRightTwoName))
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
private fun TagText(tagType: Tag) =
    when (tagType) {
        is Tag.Date -> Tag(name = "Date", text = tagType.text)
        is Tag.Tournament -> Tag(name = "Tournament", text = tagType.text)
        is Tag.Category -> Tag(name = "Category", text = tagType.text)
        is Tag.Rule -> Tag(name = "Game rule", text = tagType.text)
        is Tag.Player -> Tag(name = "Player", text = tagType.text)
    }