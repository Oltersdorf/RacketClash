package dev.oltersdorf.racketclash.database

import dev.oltersdorf.racketclash.database.api.IdItem
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.core.statements.UpdateStatement

internal class ColumnLink<T, Item: IdItem>(
    private val column: Column<T>,
    private val canUpdate: Boolean,
    private val link: (Item) -> T
) {
    fun link(builder: UpdateBuilder<Int>, data: Item) {
        if (builder !is UpdateStatement || canUpdate) builder[column] = link(data)
    }
}