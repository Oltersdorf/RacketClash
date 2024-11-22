package com.olt.racketclash.tournaments

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.state.ViewModelState

class TournamentsModel(
    private val database: Database
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

    fun onLocationSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun onCourtsSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun onStartDateTimeSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun onEndDateTimeSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun onPlayersSort(direction: SortDirection) =
        onIO {
            //update teams
        }

    fun updatePage(number: Int) =
        onIO {
            //update teams
        }
}