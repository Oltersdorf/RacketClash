package com.olt.racketclash.team

import com.olt.racketclash.database.api.TeamDatabase
import com.olt.racketclash.state.ViewModelState

class TeamModel(
    private val database: TeamDatabase,
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

    fun onNameSort() =
        onIO {
            //update players
        }

    fun onBirthYearSort() =
        onIO {
            //update players
        }

    fun onClubSort() =
        onIO {
            //update players
        }

    fun onSinglesSort() =
        onIO {
            //update players
        }

    fun onDoublesSort() =
        onIO {
            //update players
        }

    fun updatePage(number: Int) =
        onIO {
            //update players
        }
}