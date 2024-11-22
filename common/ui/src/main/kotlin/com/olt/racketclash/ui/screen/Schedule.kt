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
import com.olt.racketclash.schedule.ScheduleModel
import com.olt.racketclash.schedule.ScheduledGame
import com.olt.racketclash.schedule.Tag
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.SimpleIconButton
import com.olt.racketclash.ui.component.Status
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll

@Composable
internal fun Schedule(
    database: Database,
    tournamentId: Long
) {
    val model = remember { ScheduleModel(database = database, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Schedule",
        items = state.scheduledGames,
        isLoading = state.isLoading,
        columns = columns(
            onConfirm = model::onSaveResult,
            updateResult = model::updateResult,
            onActiveSort = model::onActiveSort,
            onScheduleSort = model::onScheduleSort,
            onTypeSort = model::onTypeSort,
            onCategorySort = model::onCategorySort
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
    onConfirm: (Long) -> Unit,
    updateResult: (scheduledGameId: Long, set: Int, isLeft: Boolean, resultString: String) -> Unit,
    onActiveSort: (SortDirection) -> Unit,
    onScheduleSort: (SortDirection) -> Unit,
    onTypeSort: (SortDirection) -> Unit,
    onCategorySort: (SortDirection) -> Unit,
): List<LazyTableColumn<ScheduledGame>> =
    listOf(
        LazyTableColumn.Builder("Active", weight = 0.05f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onActiveSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onActiveSort(SortDirection.Descending)
            }
        }) { scheduledGame, weight ->
            Status(modifier = Modifier.weight(weight), isOkay = scheduledGame.active)
        },
        LazyTableColumn.Text(name = "Schedule", weight = 0.15f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onScheduleSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onActiveSort(SortDirection.Descending)
            }
        }) { it.scheduled },
        LazyTableColumn.Text(name = "Type", weight = 0.05f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onTypeSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onActiveSort(SortDirection.Descending)
            }
        }) { if (it.single) "Single" else "Double" },
        LazyTableColumn.Text(name = "Category", weight = 0.15f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onCategorySort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onActiveSort(SortDirection.Descending)
            }
        }) { it.categoryName },
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
                        scheduledGame.sets.forEachIndexed { index, set ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    modifier = Modifier.width(100.dp),
                                    value = set.first,
                                    onValueChange = { updateResult(scheduledGame.id, index, true, it) },
                                    singleLine = true
                                )

                                Text(text = ":", modifier = Modifier.padding(horizontal = 5.dp))

                                OutlinedTextField(
                                    modifier = Modifier.width(100.dp),
                                    value = set.second,
                                    onValueChange = { updateResult(scheduledGame.id, index, false, it) },
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
private fun TagText(tagType: Tag) =
    when (tagType) {
        Tag.Active -> Tag(name = "Active")
        is Tag.Category -> Tag(name = "Category", text = tagType.text)
        Tag.Doubles -> Tag(name = "Doubles")
        is Tag.Player -> Tag(name = "Player", text = tagType.text)
        Tag.Singles -> Tag(name = "Singles")
    }