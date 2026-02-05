package dev.oltersdorf.racketclash.database

import dev.oltersdorf.racketclash.database.api.IdItem
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

internal open class LongIdTable<Item: IdItem>(name: String) : Table(name = name) {
    val id: Column<Long> = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val linkedColumns = mutableListOf<ColumnLink<*, Item>>()

    fun linkedVarchar(name: String, length: Int, canUpdate: Boolean = true, link: (Item) -> String): Column<String> =
        varchar(name = name, length = length).also { linkedColumns.add(ColumnLink(it, canUpdate, link)) }

    fun linkedInteger(name: String, canUpdate: Boolean = true, link: (Item) -> Int): Column<Int> =
        integer(name = name).also { linkedColumns.add(ColumnLink(it, canUpdate, link)) }

    fun linkedReference(name: String, refColumn: Column<Long>, canUpdate: Boolean = true, link: (Item) -> Long): Column<Long> =
        reference(name = name, refColumn = refColumn).also { linkedColumns.add(ColumnLink(it, canUpdate, link)) }
}