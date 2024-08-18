package com.olt.racketclash.data.database.mapper

import com.olt.racketclash.data.Project
import com.olt.racketclash.database.project.SelectAll

fun SelectAll.toProject() =
    Project(
        id = id,
        name = name,
        lastModified = lastModified,
        numberOfPlayers = numberOfPlayers.toInt(),
        numberOfTeams = numberOfTeams.toInt()
    )