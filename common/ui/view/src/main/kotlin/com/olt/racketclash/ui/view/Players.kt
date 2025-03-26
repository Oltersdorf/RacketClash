package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.PlayerFilter
import com.olt.racketclash.database.api.PlayerSorting
import com.olt.racketclash.players.PlayersModel
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.LazyTableSortDirection
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.layout.FilteredLazyTable
import com.olt.racketclash.ui.theme.AdditionalMaterialTheme
import org.jetbrains.compose.resources.painterResource
import racketclash.common.ui.view.generated.resources.Res
import racketclash.common.ui.view.generated.resources.medal

@Composable
internal fun Players(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { PlayersModel(database = database.players) }
    val state by model.state.collectAsState()

    FilteredLazyTable(
        state = ListState(
            isLoading = state.isLoading,
            items = state.players,
            filter = PlayerFilter(),
            sorting = PlayerSorting.NameAsc
        ),
        columns = columns(
            navigateTo = navigateTo,
            onSort = model::onSort,
            onDelete = model::deletePlayer
        ),
        onPageClicked = model::updatePage
    ) {

    }
}

private fun columns(
    navigateTo: (View) -> Unit,
    onSort: (PlayerSorting) -> Unit,
    onDelete: (Player) -> Unit
): List<LazyTableColumn<Player>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.25f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.NameDesc)
            }
        }) { navigateTo(View.Player(playerName = it.name, playerId = it.id)) },
        LazyTableColumn.Text(name = "Birth year", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.BirthYearAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.BirthYearDesc)
            }
        }) { it.birthYear.toString() },
        LazyTableColumn.Text(name = "Club", weight = 0.25f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.ClubAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.ClubDesc)
            }
        }) { it.club },
        LazyTableColumn.Text(name = "# tournaments", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.TournamentsAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.TournamentsDesc)
            }
        }) { it.numberOfTournaments.toString() },
        LazyTableColumn.Builder(name = "Medals", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.MedalsAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.MedalsDesc)
            }
        }) { player, weight ->
            Row(modifier = Modifier.weight(weight)) {
                if (player.goldMedals > 0) {
                    Text(text = player.goldMedals.toString())
                    Icon(
                        painter = painterResource(Res.drawable.medal),
                        contentDescription = "Gold",
                        tint = AdditionalMaterialTheme.current.gold
                    )
                }
                if (player.silverMedals > 0) {
                    Text(
                        text = player.silverMedals.toString(),
                        modifier = Modifier.padding(start = if (player.goldMedals > 0) 10.dp else 0.dp)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.medal),
                        contentDescription = "Silver",
                        tint = AdditionalMaterialTheme.current.silver
                    )
                }
                if (player.bronzeMedals > 0) {
                    Text(
                        text = player.bronzeMedals.toString(),
                        modifier = Modifier.padding(start = if (player.goldMedals > 0 || player.silverMedals > 0) 10.dp else 0.dp)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.medal),
                        contentDescription = "Bronze",
                        tint = AdditionalMaterialTheme.current.bronze
                    )
                }
            }
        },
        LazyTableColumn.Builder(name = "Single", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.SinglesAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.SinglesDesc)
            }
        }) { player, weight ->
            /*RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioSingle.first,
                middle = player.winRatioSingle.second,
                right = player.winRatioSingle.third
            )*/
        },
        LazyTableColumn.Builder(name = "Double", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.DoublesAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.DoublesDesc)
            }
        }) { player, weight ->
            /*RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioDouble.first,
                middle = player.winRatioDouble.second,
                right = player.winRatioDouble.third
            )*/
        },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            enabled = { it.gamesPlayed + it.gamesScheduled == 0L },
            onClick = onDelete,
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )
