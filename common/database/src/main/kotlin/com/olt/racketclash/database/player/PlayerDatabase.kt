package com.olt.racketclash.database.player

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.table.FilteredAndOrderedPlayer
import com.olt.racketclash.database.table.Player

class PlayerDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrdered(
        nameFilter: String,
        birthYearFilter: Int?,
        clubFilter: String,
        hasMedalsFilter: Boolean?,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<FilteredAndOrderedPlayer>> =
        database.playerQueries.selectFilteredAndOrderedSize(
            nameFilter = nameFilter,
            birthYearFilter = birthYearFilter,
            clubFilter = clubFilter,
            hasMedalsFilter = hasMedalsFilter?.let { if (it) 1L else 0L }
        ).executeAsOne().size to
        database.playerQueries.filteredAndOrderedPlayer(
            nameFilter = nameFilter,
            birthYearFilter = birthYearFilter,
            clubFilter = clubFilter,
            hasMedalsFilter = hasMedalsFilter?.let { if (it) 1L else 0L },
            sorting = sorting.name,
            offset = fromIndex.toLong(),
            limit = toIndex.toLong()
        ).executeAsList()


    fun selectSingle(id: Long): Player =
        database.playerQueries.player(id = id).executeAsOne()

    fun clubs(clubFilter: String) =
        database.playerQueries.clubs(clubFilter = clubFilter).executeAsList()

    fun add(player: Player) =
        database.playerQueries.add(
            name = player.name,
            birthYear = player.birthYear,
            club = player.club
        )

    fun update(player: Player) =
        database.playerQueries.update(
            id = player.id,
            name = player.name,
            birthYear = player.birthYear,
            club = player.club
        )

    fun delete(id: Long) =
        database.playerQueries.delete(id = id)
}