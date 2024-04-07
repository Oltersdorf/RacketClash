package com.olt.racketclash.database.mapper

import com.olt.racketclash.data.Team
import com.olt.racketclash.database.team.SelectAll

fun SelectAll.toTeam() =
    Team(id = id, name = name, strength = strength, size = size.toInt())