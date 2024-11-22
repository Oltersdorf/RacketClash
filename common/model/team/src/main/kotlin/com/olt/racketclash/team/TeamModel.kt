package com.olt.racketclash.team

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.state.ViewModelState

class TeamModel(
    private val database: Database,
    private val teamId: Long,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    fun updateSearchBar(text: String) {
        updateState { copy(searchBarText = text) }

        onDefault {
            updateState {
                val tags = availableTags.toMutableList()
                tags.replaceAll { it.changeText(newText = text) }

                copy(availableTags = tags.toList())
            }
        }
    }

    fun addTag(tag: Tag) {
        updateState { copy(availableTags = availableTags - tag, tags = tags + tag) }

        onIO {
            //update players
        }
    }

    fun removeTag(tag: Tag) {
        updateState {
            copy(
                tags = tags - tag,
                availableTags = availableTags + tag.changeText(newText = searchBarText)
            )
        }

        onIO {
            //update players
        }
    }

    fun onNameSort(direction: SortDirection) =
        onIO {
            //update players
        }

    fun onBirthYearSort(direction: SortDirection) =
        onIO {
            //update players
        }

    fun onClubSort(direction: SortDirection) =
        onIO {
            //update players
        }

    fun onSinglesSort(direction: SortDirection) =
        onIO {
            //update players
        }

    fun onDoublesSort(direction: SortDirection) =
        onIO {
            //update players
        }

    fun updatePage(number: Int) =
        onIO {
            //update players
        }
}