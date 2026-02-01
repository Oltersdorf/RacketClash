package dev.oltersdorf.racketclash.database

import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.LikeEscapeOp
import org.jetbrains.exposed.v1.core.like

internal infix fun Expression<String>.contains(pattern: String): LikeEscapeOp =
    like("%$pattern%")