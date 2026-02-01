package dev.oltersdorf.racketclash.database

import dev.oltersdorf.racketclash.database.api.FilteredSortedList
import dev.oltersdorf.racketclash.database.api.IdItem
import dev.oltersdorf.racketclash.database.api.TableBase
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

internal abstract class TableBaseImpl<Item: IdItem, Filter, Sorting>(name: String) : TableBase<Item, Filter, Sorting>, LongIdTable(name = name) {

    internal abstract fun updateMapper(builder: UpdateBuilder<Int>, data: Item)

    internal open fun select(): Query = selectAll()

    internal abstract fun itemMapper(row: ResultRow): Item

    internal abstract fun filterItems(filter: Filter): Op<Boolean>

    internal abstract fun sortItems(sorting: Sorting): Pair<Expression<*>, SortOrder>

    override suspend fun insert(data: Item): Long =
        transaction {
            insertReturning(listOf(this@TableBaseImpl.id)) { updateMapper(builder = it, data = data) }
                .single()[this@TableBaseImpl.id]
        }


    override suspend fun delete(id: Long): Boolean =
        transaction {
            deleteWhere { this.id eq id }
        } == 1

    override suspend fun update(data: Item): Boolean =
        transaction {
            update(where = { this@TableBaseImpl.id eq data.id }) {
                updateMapper(builder = it, data = data)
            }
        } == 1

    override suspend fun selectSingle(id: Long): Item? =
        transaction {
            select()
                .where { this@TableBaseImpl.id eq id }
                .map(::itemMapper)
                .singleOrNull()
        }

    override suspend fun selectLast(n: Int): List<Item> =
        transaction {
            select()
                .orderBy(this@TableBaseImpl.id, SortOrder.DESC)
                .limit(n)
                .map(::itemMapper)
        }

    override suspend fun selectSortedAndFiltered(
        filter: Filter,
        sorting: Sorting,
        fromIndex: Long,
        limit: Int
    ): FilteredSortedList<Item, Filter, Sorting> =
        transaction {
            FilteredSortedList(
                totalItemsInDatabase = select(this@TableBaseImpl.id).count(),
                fromIndex = fromIndex,
                limit = limit,
                filter = filter,
                sorting = sorting,
                items = select()
                    .where(filterItems(filter))
                    .orderBy(sortItems(sorting))
                    .limit(limit)
                    .offset(fromIndex)
                    .map(::itemMapper)
            )
        }
}