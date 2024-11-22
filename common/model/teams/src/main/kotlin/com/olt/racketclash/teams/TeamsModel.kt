package com.olt.racketclash.teams

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.state.ViewModelState

class TeamsModel(
    private val database: Database,
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
            //update teams
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
            //update teams
        }
    }

    fun onNameSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun onSizeSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun onSingleSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun onDoubleSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun updatePage(number: Int) =
        onIO {
            //update teams
        }
}