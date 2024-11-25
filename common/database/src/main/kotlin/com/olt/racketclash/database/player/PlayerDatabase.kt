package com.olt.racketclash.database.player

import com.olt.racketclash.database.RacketClashDatabase

class PlayerDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrdered(
        nameFilter: String,
        birthYearFilter: Int?,
        clubFilter: String,
        hasMedalsFilter: Boolean?,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<DeletablePlayer>> =
        database.playerQueries.selectFilteredAndOrderedSize(
            nameFilter = nameFilter,
            birthYearFilter = birthYearFilter,
            clubFilter = clubFilter,
            hasMedalsFilter = hasMedalsFilter?.let { if (it) 1L else 0L }
        ).executeAsOne().size to
        database.playerQueries.selectFilteredAndOrdered(
            nameFilter = nameFilter,
            birthYearFilter = birthYearFilter,
            clubFilter = clubFilter,
            hasMedalsFilter = hasMedalsFilter?.let { if (it) 1L else 0L },
            sorting = sorting.name,
            offset = fromIndex.toLong(),
            limit = toIndex.toLong()
        ).executeAsList().map { it.toDeletablePlayer() }


    fun selectSingle(id: Long): DeletablePlayer =
        database.playerQueries.selectSingle(id = id).executeAsOne().toDeletablePlayer()

    fun clubs(clubFilter: String) =
        database.playerQueries.clubs(clubFilter = clubFilter).executeAsList()

    fun add(player: DeletablePlayer) =
        database.playerQueries.add(
            name = player.name,
            birthYear = player.birthYear,
            club = player.club
        )

    fun update(player: DeletablePlayer) =
        database.playerQueries.update(
            id = player.id,
            name = player.name,
            birthYear = player.birthYear,
            club = player.club
        )

    fun delete(id: Long) =
        database.playerQueries.delete(id = id)
}