package dev.oltersdorf.racketclash.database

import dev.oltersdorf.racketclash.database.api.IdItem
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.testcontainers.containers.PostgreSQLContainer

object TestDatabase {
    val postgres = PostgreSQLContainer("postgres:18-alpine").apply {
        withDatabaseName("testdb")
        withUsername("testuser")
        withPassword("testpass")
        start()
    }

    fun connect() {
        Database.connect(
            url = postgres.jdbcUrl,
            driver = "org.postgresql.Driver",
            user = postgres.username,
            password = postgres.password
        )
    }

    data class TestData(override val id: Long, val name: String) : IdItem

    fun generateTestData(prefixName: String, count: Int = 2): List<TestData> =
        List(count) { index ->
            TestData(id = index.toLong(), name = "$prefixName$index")
        }

    data class TestFilter(val name: String)

    sealed class TestSorting {
        data object NameAsc : TestSorting()
        data object NameDesc : TestSorting()
    }

    internal object TestTable : TableBaseImpl<TestData, TestFilter, TestSorting>("test") {
        val name = varchar("name", 50)

        override fun updateMapper(builder: UpdateBuilder<Int>, data: TestData) {
            builder[name] = data.name
        }

        override fun itemMapper(row: ResultRow): TestData =
            TestData(id = row[id], name = row[name])

        override fun filterItems(filter: TestFilter): Op<Boolean> =
            name contains filter.name

        override fun sortItems(sorting: TestSorting): Pair<Expression<*>, SortOrder> =
            when (sorting) {
                TestSorting.NameAsc -> name to SortOrder.ASC
                TestSorting.NameDesc -> name to SortOrder.DESC
            }

        fun selectAllTestDataOrderedById(): List<TestData> =
            transaction { selectAll().map { TestData(id = it[this@TestTable.id], name = it[name]) } }.sortedBy { it.id }

    }
}