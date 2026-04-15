package dev.oltersdorf.racketclash.database.table

import dev.oltersdorf.racketclash.database.TableBaseImpl
import dev.oltersdorf.racketclash.database.api.TableBase
import dev.oltersdorf.racketclash.database.api.item.Player
import dev.oltersdorf.racketclash.database.api.item.PlayerFilter
import dev.oltersdorf.racketclash.database.api.item.PlayerSorting
import dev.oltersdorf.racketclash.database.api.item.PlayerTable
import dev.oltersdorf.racketclash.database.contains
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

internal object Players : TableBaseImpl<Player, PlayerFilter, PlayerSorting>("players") {
    val name: Column<String> = linkedVarchar("name", 100) { it.name }
    val birthYear: Column<Int> = linkedInteger("birthYear") { it.birthYear }
    val clubId: Column<Long> = linkedReference("clubId", Clubs.id) { it.clubId }

    override fun select(): Query =
        innerJoin(Clubs)
            .select(id, name, birthYear, clubId, Clubs.name)

    override fun itemMapper(row: ResultRow): Player =
        Player(
            id = row[id],
            name = row[name],
            birthYear = row[birthYear],
            clubId = row[clubId],
            clubName = row[Clubs.name]
        )

    override fun filterItems(filter: PlayerFilter): Op<Boolean> =
        (name contains filter.name) and
        (birthYear greaterEq filter.birthYearStart) and
        (birthYear lessEq filter.birthYearEnd) and
        (Clubs.name contains filter.club)

    override fun sortItems(sorting: PlayerSorting): Pair<Expression<*>, SortOrder> =
        when (sorting) {
            PlayerSorting.NameAsc       -> name       to SortOrder.ASC
            PlayerSorting.NameDesc      -> name       to SortOrder.DESC
            PlayerSorting.BirthYearAsc  -> birthYear  to SortOrder.ASC
            PlayerSorting.BirthYearDesc -> birthYear  to SortOrder.DESC
            PlayerSorting.ClubAsc       -> Clubs.name to SortOrder.ASC
            PlayerSorting.ClubDesc      -> Clubs.name to SortOrder.DESC
        }
}

object PlayerTableInterface : PlayerTable, TableBase<Player, PlayerFilter, PlayerSorting> by Players {
    override suspend fun clubs(filter: String): List<String> =
        transaction {
            Clubs
                .select(Clubs.name)
                .where { Clubs.name contains filter }
                .map { it[Clubs.name] }
        }
}