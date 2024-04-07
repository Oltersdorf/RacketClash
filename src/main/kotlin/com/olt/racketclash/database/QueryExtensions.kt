package com.olt.racketclash.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <Q: Any, T> Query<Q>.mapToSingle(mapper: (Q?) -> T?) : Flow<T?> =
    asFlow()
        .mapToOneOrNull(context = Dispatchers.IO)
        .map(transform = mapper)

fun <Q: Any, T> Query<Q>.mapToList(mapper: (Q) -> T) : Flow<List<T>> =
    asFlow()
        .mapToList(context = Dispatchers.IO)
        .map { it.map(mapper) }