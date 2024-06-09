package com.olt.racketclash.data.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.data.Round
import com.olt.racketclash.database.RoundQueries
import com.olt.racketclash.database.RoundTable
import com.olt.racketclash.data.database.mapper.toRound
import kotlinx.coroutines.flow.Flow

class RoundsDatabase(private val queries: RoundQueries) {

    companion object {
        val roundAdapter by lazy { RoundTable.Adapter(orderNumberAdapter = IntColumnAdapter) }
    }

    fun rounds(): Flow<List<Round>> =
        queries
            .selectAll()
            .mapToList { it.toRound() }

    fun round(id: Long): Flow<Round?> =
        queries
            .select(id = id)
            .mapToSingle { it?.toRound() }

    fun addRound(name: String) =
        queries.add(name = name)

    fun deleteRound(id: Long) = queries.delete(id = id)

    fun updateRoundName(id: Long, name: String) = queries.updateName(id = id, name = name)

    fun lastInsertedRow(): Long? = queries.lastInsertRowId().executeAsOneOrNull()
}