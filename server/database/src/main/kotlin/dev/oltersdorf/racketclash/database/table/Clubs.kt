package dev.oltersdorf.racketclash.database.table

import dev.oltersdorf.racketclash.database.TableBaseImpl
import dev.oltersdorf.racketclash.database.api.TableBase
import dev.oltersdorf.racketclash.database.api.item.Club
import dev.oltersdorf.racketclash.database.api.item.ClubFilter
import dev.oltersdorf.racketclash.database.api.item.ClubSorting
import dev.oltersdorf.racketclash.database.api.item.ClubTable
import dev.oltersdorf.racketclash.database.contains
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder

internal object Clubs : TableBaseImpl<Club, ClubFilter, ClubSorting>("clubs") {
    val name: Column<String> = linkedVarchar("name", 100) { it.name }

    override fun itemMapper(row: ResultRow): Club =
        Club(
            id = row[id],
            name = row[name]
        )

    override fun filterItems(filter: ClubFilter): Op<Boolean> =
        name contains filter.name

    override fun sortItems(sorting: ClubSorting): Pair<Expression<*>, SortOrder> =
        when (sorting) {
            ClubSorting.NameAsc  -> name to SortOrder.ASC
            ClubSorting.NameDesc -> name to SortOrder.DESC
        }
}

object ClubTableInterface : ClubTable, TableBase<Club, ClubFilter, ClubSorting> by Clubs