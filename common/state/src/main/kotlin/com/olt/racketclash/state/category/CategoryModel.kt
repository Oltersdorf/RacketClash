package com.olt.racketclash.state.category

import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.CategoryDatabase
import com.olt.racketclash.state.detail.DetailModel

class CategoryModel(
    private val categoryDatabase: CategoryDatabase,
    private val categoryId: Long
) : DetailModel<Category, CategoryData>(
    initialItem = Category(),
    initialData = CategoryData()
) {

    override suspend fun databaseUpdate(item: Category) =
        categoryDatabase.update(category = item)

    override suspend fun databaseSelectItem(): Category =
        categoryDatabase.selectSingle(id = categoryId)

    override suspend fun databaseSelectData(item: Category): CategoryData = CategoryData()
}