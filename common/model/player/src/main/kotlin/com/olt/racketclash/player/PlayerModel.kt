package com.olt.racketclash.player

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.state.ViewModelState

class PlayerModel(
    private val database: Database,
    private val playerId: Long
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
            //update games
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
            //update games
        }
    }

    fun onDateSort(direction: SortDirection) =
        onIO {
            //update games
        }

    fun onTournamentSort(direction: SortDirection) =
        onIO {
            //update games
        }

    fun onCategorySort(direction: SortDirection) =
        onIO {
            //update games
        }

    fun updatePage(number: Int) =
        onIO {
            //update games
        }
}