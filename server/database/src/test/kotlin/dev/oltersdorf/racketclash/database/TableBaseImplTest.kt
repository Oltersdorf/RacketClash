package dev.oltersdorf.racketclash.database

import dev.oltersdorf.racketclash.database.TestDatabase.generateTestData
import dev.oltersdorf.racketclash.database.api.FilteredSortedList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TableBaseImplTest : BaseDataTest() {
    @Test
    fun `insert test`() {
        var expectedData = generateTestData(prefixName = "insert", count = 1)[0]

        val expectedId = runBlocking { TestDatabase.TestTable.insert(expectedData) }

        expectedData = expectedData.copy(id = expectedId)

        val actualData = TestDatabase.TestTable.selectAllTestDataOrderedById()

        assertEquals(actualData.size, 1)
        assertEquals(expectedData, actualData.first())
    }

    @Test
    fun `delete test`() {
        val testData = generateTestData(prefixName = "delete")

        TestDatabase.TestTable.batchInsertTestData(testData)

        val deleteWasSuccess = runBlocking { TestDatabase.TestTable.delete(testData[0].id) }

        val databaseContent = TestDatabase.TestTable.selectAllTestDataOrderedById()

        assertTrue(deleteWasSuccess)
        assertEquals(1, databaseContent.size)
        assertEquals(testData[1], databaseContent.first())
    }

    @Test
    fun `update test`() {
        val testData = generateTestData(prefixName = "update").toMutableList()

        TestDatabase.TestTable.batchInsertTestData(testData)

        testData[0] = testData[0].copy(name = "${testData[0].name} updated")

        val updateWasSuccess = runBlocking { TestDatabase.TestTable.update(testData[0].copy(unchangeable = "changed")) }

        val databaseContent = TestDatabase.TestTable.selectAllTestDataOrderedById()

        assertTrue(updateWasSuccess)
        assertContentEquals(testData, databaseContent)
    }

    @Test
    fun `select single test`() {
        val testData = generateTestData(prefixName = "selectSingle")

        TestDatabase.TestTable.batchInsertTestData(testData)

        val selectedData = runBlocking { TestDatabase.TestTable.selectSingle(testData[0].id) }

        val databaseContent = TestDatabase.TestTable.selectAllTestDataOrderedById()

        assertContentEquals(testData, databaseContent)
        assertEquals(testData[0], selectedData)
    }

    @Test
    fun `select last n tests`() {
        val testData = generateTestData(prefixName = "selectLastN", count = 5)

        TestDatabase.TestTable.batchInsertTestData(testData)

        val selectedData = runBlocking { TestDatabase.TestTable.selectLast(3) }

        val databaseContent = TestDatabase.TestTable.selectAllTestDataOrderedById()

        assertContentEquals(testData, databaseContent)
        assertEquals(testData.reversed().take(3), selectedData)
    }

    @Test
    fun `select sorted and filtered tests`() {
        val testData = listOf(
            TestDatabase.TestData(id = 0, name = "sortedAndFiltered0"),
            TestDatabase.TestData(id = 1, name = "sortedAndFiltered1 findMe"),
            TestDatabase.TestData(id = 2, name = "sortedAndFiltered2"),
            TestDatabase.TestData(id = 3, name = "sortedAndFiltered3 findMe"),
            TestDatabase.TestData(id = 4, name = "sortedAndFiltered4"),
            TestDatabase.TestData(id = 5, name = "sortedAndFiltered5 findMe"),
            TestDatabase.TestData(id = 6, name = "sortedAndFiltered6"),
            TestDatabase.TestData(id = 7, name = "sortedAndFiltered7 findMe")
        )
        val expectedData = FilteredSortedList<TestDatabase.TestData, TestDatabase.TestFilter, TestDatabase.TestSorting>(
            totalItemsInDatabase = 8,
            fromIndex = 1,
            limit = 2,
            filter = TestDatabase.TestFilter(name = "findMe"),
            sorting = TestDatabase.TestSorting.NameDesc,
            items = listOf(testData[5], testData[3])
        )

        TestDatabase.TestTable.batchInsertTestData(testData)

        val selectedData = runBlocking {
            TestDatabase.TestTable.selectSortedAndFiltered(
                filter = expectedData.filter,
                sorting = expectedData.sorting,
                fromIndex = expectedData.fromIndex,
                limit = expectedData.limit
            )
        }

        val databaseContent = TestDatabase.TestTable.selectAllTestDataOrderedById()

        assertContentEquals(testData, databaseContent)
        assertEquals(expectedData, selectedData)
    }
}