package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.*
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens
import com.olt.racketclash.ui.theme.AdditionalMaterialTheme
import org.jetbrains.compose.resources.painterResource
import racketclash.common.ui.generated.resources.Res
import racketclash.common.ui.generated.resources.medal

private data class Player(
    val id: Long = 0L,
    val name: String = "Test Player",
    val birthYear: Int = 1900,
    val club: String = "Test club",
    val numberOfTournaments: Int = 3,
    val goldMedals: Int = 1,
    val silverMedals: Int = 3,
    val bronzeMedals: Int = 2,
    val winRatioSingle: Triple<Int, Int, Int> = Triple(10, 0, 20),
    val winRatioDouble: Triple<Int, Int, Int> = Triple(15, 0, 12)
)

private sealed class TagTypePlayer {
    data class Name(val text: String) : TagTypePlayer()
    data class BirthYear(val text: String) : TagTypePlayer()
    data class Club(val text: String) : TagTypePlayer()
    data object HasMedals : TagTypePlayer()
    data object HasNoMedals : TagTypePlayer()
}

@Composable
internal fun Players(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    var searchBarText by remember { mutableStateOf("1") }
    var availableTags by remember { mutableStateOf( listOf(
        TagTypePlayer.HasMedals,
        TagTypePlayer.HasNoMedals,
        TagTypePlayer.Name("1"),
        TagTypePlayer.BirthYear("1"),
        TagTypePlayer.Club("1")
    )) }
    var tags by remember { mutableStateOf(listOf(
        TagTypePlayer.HasMedals,
        TagTypePlayer.HasNoMedals,
        TagTypePlayer.Name("1"),
        TagTypePlayer.BirthYear("1"),
        TagTypePlayer.Club("1")
    )) }
    var players by remember { mutableStateOf(listOf(
        Player(),
        Player(goldMedals = 0, winRatioSingle = Triple(1, 0, 100)),
        Player(winRatioSingle = Triple(0, 0, 5)),
        Player(winRatioSingle = Triple(10, 10, 23)),
        Player(winRatioSingle = Triple(10, 10, 100))
    )) }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(2) }
    var isLoading by remember { mutableStateOf(false) }

    SearchableLazyTableWithScroll(
        title = "Players",
        onTitleAdd = { navigateTo(Screens.AddOrUpdatePlayer(playerName = null, playerId = null)) },
        items = players,
        isLoading = isLoading,
        columns = columns(navigateTo = navigateTo),
        currentPage = currentPage,
        lastPage = lastPage,
        onPageClicked = { currentPage = it }
    ) {
        SearchBar(
            text = searchBarText,
            onTextChange = { searchBarText = it },
            dropDownItems = availableTags,
            onDropDownItemClick = { tags += it },
            tags = tags,
            onTagRemove = { tags -= it },
            tagText = { TagText(it) }
        )
    }
}

private fun columns(navigateTo: (Screens) -> Unit): List<LazyTableColumn<Player>> =
    listOf(
        LazyTableColumn.Builder(name = "Name", weight = 0.3f) { player, weight ->
            Link(modifier = Modifier.weight(weight), text = player.name) {
                navigateTo(Screens.AddOrUpdatePlayer(playerName = player.name, playerId = player.id))
            }
        },
        LazyTableColumn.Text(name = "Birth year", weight = 0.1f) { it.birthYear.toString() },
        LazyTableColumn.Text(name = "Club", weight = 0.3f) { it.club },
        LazyTableColumn.Text(name = "# tournaments", weight = 0.1f) { it.numberOfTournaments.toString() },
        LazyTableColumn.Builder(name = "Medals", weight = 0.1f) { player, weight ->
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
        LazyTableColumn.Builder(name = "Single", weight = 0.1f) { player, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioSingle.first,
                middle = player.winRatioSingle.second,
                right = player.winRatioSingle.third
            )
        },
        LazyTableColumn.Builder(name = "Double", weight = 0.1f) { player, weight ->
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
private fun TagText(tagType: TagTypePlayer) =
    when (tagType) {
        is TagTypePlayer.BirthYear -> Tag(name = "Birth year", text = tagType.text)
        is TagTypePlayer.Club -> Tag(name = "Club", text = tagType.text)
        TagTypePlayer.HasMedals -> Tag(name = "Has medals")
        TagTypePlayer.HasNoMedals -> Tag(name = "Has no medals")
        is TagTypePlayer.Name -> Tag(name = "Name", text = tagType.text)
    }