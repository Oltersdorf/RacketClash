package com.olt.racketclash.data.database.mapper

import com.olt.racketclash.data.ProjectSettings
import com.olt.racketclash.database.SelectSettings

fun SelectSettings.toProjectSettings(): ProjectSettings =
    ProjectSettings(
        id = id,
        fields = fields,
        timeout = timeout,
        gamePointsForBye = gamePointsForBye,
        setPointsForBye = setPointsForBye,
        pointsForBye = pointsForBye
    )