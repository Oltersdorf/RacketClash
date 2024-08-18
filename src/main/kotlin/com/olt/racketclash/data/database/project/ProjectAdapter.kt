package com.olt.racketclash.data.database.project

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.ProjectTable

fun projectAdapter() = ProjectTable.Adapter(
    fieldsAdapter = IntColumnAdapter,
    timeoutAdapter = IntColumnAdapter,
    gamePointsForByeAdapter = IntColumnAdapter,
    setPointsForByeAdapter = IntColumnAdapter,
    pointsForByeAdapter = IntColumnAdapter
)