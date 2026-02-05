package dev.oltersdorf.racketclash.database

import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class ExpressionTest : BaseDataTest() {
    @Test
    fun `contains extension should find all Strings with pattern`() {
        transaction {
            val testPattern = "test"
            val namesWithPattern = listOf(
                "$testPattern start",
                "end $testPattern",
                "middle $testPattern middle",
                testPattern
            )
            val namesWithoutPattern = listOf(
                "nothing",
                "almost tes"
            )

            namesWithPattern.forEach { testName ->
                TestDatabase.TestTable.insert {
                    it[TestDatabase.TestTable.name] = testName
                    it[TestDatabase.TestTable.unchangeable] = ""
                }
            }
            namesWithoutPattern.forEach { testName ->
                TestDatabase.TestTable.insert {
                    it[TestDatabase.TestTable.name] = testName
                    it[TestDatabase.TestTable.unchangeable] = ""
                }
            }

            val selectResult = TestDatabase.TestTable
                .selectAll()
                .where { TestDatabase.TestTable.name contains testPattern }
                .map { it[TestDatabase.TestTable.name] }

            assertContentEquals(expected = namesWithPattern, actual = selectResult)
        }
    }
}