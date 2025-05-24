package com.olt.racketclash.state.player

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.list.ListModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayerTableModel(
    private val database: PlayerDatabase
) : ListModel<Player, PlayerFilter, PlayerSorting>(
    emptyItem = Player(),
    initialFilter = PlayerFilter(),
    initialSorting = PlayerSorting.NameAsc
) {
    private val _clubSuggestionsState: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val clubSuggestionsState: StateFlow<List<String>> = _clubSuggestionsState.asStateFlow()

    override suspend fun databaseAdd(item: Player) =
        database.add(player = item)

    override suspend fun databaseSelect(
        filter: PlayerFilter,
        sorting: PlayerSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Player, PlayerFilter, PlayerSorting> =
        database.selectList(
            filter = filter,
            sorting = sorting,
            fromIndex = fromIndex,
            toIndex = toIndex
        )

    override suspend fun databaseDelete(item: Player) =
        database.delete(id = item.id)

    fun clubSuggestions(filter: String) {
        onIO {
            _clubSuggestionsState.update {
                database.clubs(filter = filter)
            }
        }
    }
}