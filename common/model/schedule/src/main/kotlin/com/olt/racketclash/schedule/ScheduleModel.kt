package com.olt.racketclash.schedule

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.state.ViewModelState

class ScheduleModel(
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
            //update scheduledGame
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
            //update scheduledGame
        }
    }

    fun updateResult(
        scheduledGameId: Long,
        set: Int,
        isLeft: Boolean,
        resultString: String
    ) {
        val result = resultString.toIntOrNull()
        if (result != null || resultString.isEmpty()) {
            updateState {
                val gameIndex = scheduledGames.indexOfFirst { it.id == scheduledGameId }
                if (gameIndex == -1) return@updateState this

                val oldGame = scheduledGames[gameIndex]
                val newSets = oldGame.sets.toMutableList().apply {
                    this[set] = if (isLeft)
                        this[set].copy(first = resultString)
                    else
                        this[set].copy(second = resultString)
                }

                copy(
                    scheduledGames = scheduledGames
                        .toMutableList()
                        .apply { this[gameIndex] = oldGame.copy(sets = newSets) }
                )
            }
        }
    }

    fun onActiveSort(direction: SortDirection) =
        onIO {
            //update scheduledGame
        }

    fun onScheduleSort(direction: SortDirection) =
        onIO {
            //update scheduledGame
        }

    fun onTypeSort(direction: SortDirection) =
        onIO {
            //update scheduledGame
        }

    fun onCategorySort(direction: SortDirection) =
        onIO {
            //update scheduledGame
        }

    fun updatePage(number: Int) =
        onIO {
            //update scheduledGame
        }

    fun onSaveResult(scheduledGameId: Long) =
        onIO {
            //write to database
        }
}