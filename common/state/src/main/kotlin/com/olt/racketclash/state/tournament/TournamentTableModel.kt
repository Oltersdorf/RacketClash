package com.olt.racketclash.state.tournament

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.list.ListModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TournamentTableModel(
    private val database: TournamentDatabase
) : ListModel<Tournament, TournamentFilter, TournamentSorting>(
    emptyItem = Tournament(),
    initialFilter = TournamentFilter(),
    initialSorting = TournamentSorting.NameAsc
) {
    private val _locationSuggestionsState: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val locationSuggestionsState: StateFlow<List<String>> = _locationSuggestionsState.asStateFlow()

    override suspend fun databaseDelete(item: Tournament) =
        database.delete(id = item.id)

    override suspend fun databaseSelect(
        filter: TournamentFilter,
        sorting: TournamentSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Tournament, TournamentFilter, TournamentSorting> =
        database.selectList(
            filter = filter,
            sorting = sorting,
            fromIndex = fromIndex,
            toIndex = toIndex
        )

    override suspend fun databaseAdd(item: Tournament) =
        database.add(tournament = item)

    fun locationSuggestions(filter: String) {
        onIO {
            _locationSuggestionsState.update {
                database.locations(filter = filter)
            }
        }
    }
}