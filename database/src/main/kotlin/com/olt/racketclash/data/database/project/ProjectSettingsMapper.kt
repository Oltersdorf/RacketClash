package com.olt.racketclash.data.database.project

import com.olt.racketclash.data.ProjectSettings
import com.olt.racketclash.database.SelectSettings

internal fun SelectSettings.toProjectSettings(): ProjectSettings =
    ProjectSettings(
        id = id,
        fields = fields,
        timeout = timeout,
        gamePointsForBye = gamePointsForBye,
        setPointsForBye = setPointsForBye,
        pointsForBye = pointsForBye
    )