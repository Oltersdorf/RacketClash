package com.olt.racketclash.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.*
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens
import com.olt.racketclash.ui.theme.AdditionalMaterialTheme

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

private sealed class TagType {
    data class Name(val text: String) : TagType()
    data class BirthYear(val text: String) : TagType()
    data class Club(val text: String) : TagType()
    data object HasMedals : TagType()
    data object HasNoMedals : TagType()
}

@Composable
internal fun Players(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    var searchBarText by remember { mutableStateOf("1") }
    var availableTags by remember { mutableStateOf( listOf(
        TagType.HasMedals,
        TagType.HasNoMedals,
        TagType.Name("1"),
        TagType.BirthYear("1"),
        TagType.Club("1")
    )) }
    var tags by remember { mutableStateOf(listOf(
        TagType.HasMedals,
        TagType.HasNoMedals,
        TagType.Name("1"),
        TagType.BirthYear("1"),
        TagType.Club("1")
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(
            label = "Filter",
            text = searchBarText,
            onTextChange = { searchBarText = it },
            dropDownItems = availableTags,
            onDropDownItemClick = { tags += it },
            tags = tags,
            onTagRemove = { tags -= it },
            tagText = { tagText(it) }
        )

        LazyTableWithScroll(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainer).weight(1.0f, fill = false),
            header = {
                Text(
                    text = "Players",
                    modifier = Modifier.weight(1.0f).padding(start = 10.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                SimpleIconButton(
                    modifier = Modifier.padding(top = 5.dp, end = 5.dp, bottom = 5.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                ) { navigateTo(Screens.AddOrUpdatePlayer(playerName = null, playerId = null)) }
            },
            items = players,
            isLoading = false,
            columns = listOf(
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
                                painter = painterResource("medal.svg"),
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
                                painter = painterResource("medal.svg"),
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
                                painter = painterResource("medal.svg"),
                                contentDescription = "Bronze",
                                tint = AdditionalMaterialTheme.current.bronze
                            )
                        }
                    }
                },
                LazyTableColumn.Builder(name = "Single", weight = 0.05f) { player, weight ->
                    RatioBar(
                        modifier = Modifier
                            .weight(weight)
                            .padding(horizontal = 5.dp),
                        left = player.winRatioSingle.first,
                        middle = player.winRatioSingle.second,
                        right = player.winRatioSingle.third
                    )
                },
                LazyTableColumn.Builder(name = "Double", weight = 0.05f) { player, weight ->
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
        )

        if (lastPage != 1)
            PageSelector(
                currentPage = currentPage,
                lastPage = lastPage,
                onPageClicked = { currentPage = it }
            )
    }
}

@Composable
private fun tagText(tagType: TagType) {
    when (tagType) {
        is TagType.BirthYear -> Row {
            Text(text = "Birth year: ", fontWeight = FontWeight.Bold)
            Text(text = "\"${tagType.text}\"")
        }
        is TagType.Club -> Row {
            Text(text = "Club: ", fontWeight = FontWeight.Bold)
            Text(text = "\"${tagType.text}\"")
        }
        TagType.HasMedals -> Text(text = "Has medals", fontWeight = FontWeight.Bold)
        TagType.HasNoMedals -> Text(text = "Has no medals", fontWeight = FontWeight.Bold)
        is TagType.Name -> Row {
            Text(text = "Name: ", fontWeight = FontWeight.Bold)
            Text(text = "\"${tagType.text}\"")
        }
    }
}