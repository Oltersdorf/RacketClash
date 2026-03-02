package dev.oltersdorf.racketclash.database

import dev.oltersdorf.racketclash.database.table.Clubs
import dev.oltersdorf.racketclash.database.table.Players
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun databasePostgresConnect(
    url: String,
    user: String,
    password: String
) {
    Database.connect(
        url = url,
        driver = "org.postgresql.Driver",
        user = user,
        password = password
    )

    transaction {
        SchemaUtils.create(
            Clubs,
            Players
        )
    }
}