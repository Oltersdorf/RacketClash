package com.olt.racketclash.state.detail

import com.olt.racketclash.database.api.Validateable

data class DetailState<Item: Validateable, Data>(
    val isLoading: Boolean = true,
    val item: Item,
    val updatedItem: Item = item,
    val canUpdate: Boolean = updatedItem.validate(),
    val data: Data
)