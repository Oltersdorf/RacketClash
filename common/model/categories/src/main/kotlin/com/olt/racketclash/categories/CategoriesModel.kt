package com.olt.racketclash.categories

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.state.ViewModelState

class CategoriesModel(
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
            //update categories
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
            //update categories
        }
    }

    fun onNameSort(direction: SortDirection) =
        onIO {
            //update categories
        }

    fun onPlayerSort(direction: SortDirection) =
        onIO {
            //update categories
        }

    fun onStatusSort(direction: SortDirection) =
        onIO {
            //update categories
        }

    fun updatePage(number: Int) =
        onIO {
            //update categories
        }
}