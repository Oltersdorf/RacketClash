package dev.oltersdorf.racketclash.database

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

internal open class LongIdTable(name: String) : Table(name = name) {
    val id: Column<Long> = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)
}