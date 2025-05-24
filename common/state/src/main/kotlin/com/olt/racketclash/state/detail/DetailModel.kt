package com.olt.racketclash.state.detail

import com.olt.racketclash.database.api.Validateable
import com.olt.racketclash.state.ViewModelState

abstract class DetailModel<Item: Validateable, Data>(
    initialItem: Item,
    initialData: Data,
): ViewModelState<DetailState<Item, Data>>(
    initialState = DetailState(
        item = initialItem,
        data = initialData
    )
) {
    init {
        onIO {
            val item = databaseSelectItem()
            val data = databaseSelectData(item = item)

            updateState {
                copy(
                    isLoading = false,
                    item = item,
                    updatedItem = item,
                    canUpdate = item.validate(),
                    data = data
                )
            }
        }
    }

    fun setUpdatedItem(item: Item) =
        updateState { copy(updatedItem = item, canUpdate = item.validate()) }

    fun applyUpdatedItem() {
        onIO {
            updateState { copy(item = updatedItem) }
            databaseUpdate(item = state.value.updatedItem)
        }
    }

    protected abstract suspend fun databaseUpdate(item: Item)

    protected abstract suspend fun databaseSelectItem(): Item

    protected abstract suspend fun databaseSelectData(item: Item): Data

    protected fun updateItem(block: Item.() -> Item) =
        updateState { copy(item = item.block()) }

    protected fun updateData(block: Data.() -> Data) =
        updateState { copy(data = data.block()) }
}