package dev.oltersdorf.racketclash.database

import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach

abstract class BaseDataTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setupDatabase() {
            TestDatabase.connect()

            transaction {
                SchemaUtils.create(TestDatabase.TestTable)
            }
        }
    }

    @BeforeEach
    fun clearData() {
        transaction { TestDatabase.TestTable.deleteAll() }
    }
}