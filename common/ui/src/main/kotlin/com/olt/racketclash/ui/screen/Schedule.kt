package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.SimpleIconButton
import com.olt.racketclash.ui.component.Status
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll

private data class ScheduledGame(
    val id: Long = 0L,
    val active: Boolean = false,
    val scheduled: String = "01 Jan 2024 10:00",
    val single: Boolean = false,
    val sets: Int = 3,
    val categoryName: String = "Test Category",
    val playerLeftOneName: String = "Player Left One",
    val playerLeftTwoName: String = "Player Left Two",
    val playerRightOneName: String = "Player Right One",
    val playerRightTwoName: String = "Player Right Two"
)

private sealed class TagTypeScheduledGame {
    data object Active : TagTypeScheduledGame()
    data object Singles : TagTypeScheduledGame()
    data object Doubles : TagTypeScheduledGame()
    data class Category(val text: String) : TagTypeScheduledGame()
    data class Player(val text: String) : TagTypeScheduledGame()
}

@Composable
internal fun Schedule(
    database: Database,
    tournamentId: Long
) {
    var searchBarText by remember { mutableStateOf("1") }
    var availableTags by remember { mutableStateOf( listOf(
        TagTypeScheduledGame.Category("1"),
        TagTypeScheduledGame.Player("1"),
        TagTypeScheduledGame.Active,
        TagTypeScheduledGame.Singles,
        TagTypeScheduledGame.Doubles
    )) }
    var tags by remember { mutableStateOf(listOf(
        TagTypeScheduledGame.Category("1"),
        TagTypeScheduledGame.Player("1"),
        TagTypeScheduledGame.Active,
        TagTypeScheduledGame.Singles,
        TagTypeScheduledGame.Doubles
    )) }
    var scheduledGames by remember { mutableStateOf(listOf(
        ScheduledGame(),
        ScheduledGame(active = true),
        ScheduledGame(single = true),
        ScheduledGame(sets = 1),
        ScheduledGame(single = true, sets = 1),
        ScheduledGame()
    )) }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(2) }
    var isLoading by remember { mutableStateOf(false) }

    SearchableLazyTableWithScroll(
        title = "Schedule",
        items = scheduledGames,
        isLoading = isLoading,
        columns = columns(onConfirm = {}),
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

private fun columns(
    onConfirm: (Long) -> Unit
): List<LazyTableColumn<ScheduledGame>> =
    listOf(
        LazyTableColumn.Builder("Active", weight = 0.05f) { scheduledGame, weight ->
            Status(modifier = Modifier.weight(weight), isOkay = scheduledGame.active)
        },
        LazyTableColumn.Text(name = "Schedule", weight = 0.15f) { it.scheduled },
        LazyTableColumn.Text(name = "Type", weight = 0.05f) { if (it.single) "Single" else "Double" },
        LazyTableColumn.Text(name = "Category", weight = 0.15f) { it.categoryName },
        LazyTableColumn.Builder(name = "Left", weight = 0.2f) { scheduledGame, weight ->
            Column(modifier = Modifier.weight(weight)) {
                Text(scheduledGame.playerLeftOneName)
                if (!scheduledGame.single)
                    Text(scheduledGame.playerLeftTwoName)
            }
        },
        LazyTableColumn.Builder(name = "Result", weight = 0.2f) { scheduledGame, weight ->
            Column(modifier = Modifier.weight(weight)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        repeat(scheduledGame.sets) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                var leftPoints by remember { mutableStateOf("") }
                                OutlinedTextField(
                                    modifier = Modifier.width(100.dp),
                                    value = leftPoints,
                                    onValueChange = { if (it.toIntOrNull() != null || it.isEmpty()) leftPoints = it },
                                    singleLine = true
                                )

                                Text(text = ":", modifier = Modifier.padding(horizontal = 5.dp))

                                var rightPoints by remember { mutableStateOf("") }
                                OutlinedTextField(
                                    modifier = Modifier.width(100.dp),
                                    value = rightPoints,
                                    onValueChange = { if (it.toIntOrNull() != null || it.isEmpty()) rightPoints = it },
                                    singleLine = true
                                )
                            }
                        }
                    }

                    SimpleIconButton(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirm"
                    ) { onConfirm(scheduledGame.id) }
                }
            }
        },
        LazyTableColumn.Builder(name = "Right", weight = 0.2f) { scheduledGame, weight ->
            Column(modifier = Modifier.weight(weight)) {
                Text(scheduledGame.playerRightOneName)
                if (!scheduledGame.single)
                    Text(scheduledGame.playerRightTwoName)
            }
        }
    )

@Composable
private fun TagText(tagType: TagTypeScheduledGame) =
    when (tagType) {
        TagTypeScheduledGame.Active -> Tag(name = "Active")
        is TagTypeScheduledGame.Category -> Tag(name = "Category", text = tagType.text)
        TagTypeScheduledGame.Doubles -> Tag(name = "Doubles")
        is TagTypeScheduledGame.Player -> Tag(name = "Player", text = tagType.text)
        TagTypeScheduledGame.Singles -> Tag(name = "Singles")
    }