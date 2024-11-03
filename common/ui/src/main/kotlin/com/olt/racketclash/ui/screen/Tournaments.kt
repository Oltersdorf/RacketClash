package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

private data class Tournament(
    val id: Long = 0L,
    val name: String = "Test tournament",
    val numberOfCourts: Int = 6,
    val location: String = "Test location",
    val startDateTime: String = "01 Jan 2024 10:00",
    val endDateTime: String = "02 Jan 2024 16:00",
    val playersCount: Int = 205,
    val categoriesCount: Int = 10
)

private sealed class TagTypeTournament {
    data class Name(val text: String) : TagTypeTournament()
    data class Location(val text: String) : TagTypeTournament()
    data class Year(val text: String) : TagTypeTournament()
}

@Composable
internal fun Tournaments(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    var tournaments by remember { mutableStateOf(listOf(Tournament(), Tournament())) }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var searchBarText by remember { mutableStateOf("1") }
    var availableTags by remember { mutableStateOf(listOf(
        TagTypeTournament.Name("1"),
        TagTypeTournament.Location("1"),
        TagTypeTournament.Year("1")
    )) }
    var tags by remember { mutableStateOf(listOf(
        TagTypeTournament.Name("1"),
        TagTypeTournament.Location("1"),
        TagTypeTournament.Year("1")
    )) }

    SearchableLazyTableWithScroll(
        title = "Tournaments",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateTournament(tournamentName = null, tournamentId = null)) },
        items = tournaments,
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

private fun columns(navigateTo: (Screens) -> Unit): List<LazyTableColumn<Tournament>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.25f, text = { it.name }) {
            navigateTo(Screens.AddOrUpdateTournament(tournamentName = it.name, tournamentId = it.id))
        },
        LazyTableColumn.Text(name = "Location", weight = 0.25f) { it.location },
        LazyTableColumn.Text(name = "Courts", weight = 0.1f) { it.numberOfCourts.toString() },
        LazyTableColumn.Text(name = "Start", weight = 0.1f) { it.startDateTime },
        LazyTableColumn.Text(name = "End", weight = 0.1f) { it.endDateTime },
        LazyTableColumn.Text(name = "Players", weight = 0.1f) { it.playersCount.toString() },
        LazyTableColumn.Text(name = "Categories", weight = 0.1f) { it.categoriesCount.toString() }
    )

@Composable
private fun TagText(tagType: TagTypeTournament) =
    when (tagType) {
        is TagTypeTournament.Year -> Tag(name = "Date", text = tagType.text)
        is TagTypeTournament.Location -> Tag(name = "Location", text = tagType.text)
        is TagTypeTournament.Name -> Tag(name = "Name", text = tagType.text)
    }