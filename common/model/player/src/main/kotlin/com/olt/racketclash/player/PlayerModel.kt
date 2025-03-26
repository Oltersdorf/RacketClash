package com.olt.racketclash.player

import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.state.ViewModelState

class PlayerModel(
    private val database: PlayerDatabase,
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

    fun onDateSort() =
        onIO {
            //update games
        }

    fun onTournamentSort() =
        onIO {
            //update games
        }

    fun onCategorySort() =
        onIO {
            //update games
        }

    fun updatePage(number: Int) =
        onIO {
            //update games
        }
}