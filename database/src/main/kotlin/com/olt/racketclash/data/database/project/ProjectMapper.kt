package com.olt.racketclash.data.database.project

import com.olt.racketclash.data.Project
import com.olt.racketclash.database.project.SelectAll

internal fun SelectAll.toProject() =
    Project(
        id = id,
        name = name,
        lastModified = lastModified,
        numberOfPlayers = numberOfPlayers.toInt(),
        numberOfTeams = numberOfTeams.toInt()
    )