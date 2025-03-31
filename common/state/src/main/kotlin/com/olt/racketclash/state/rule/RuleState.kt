package com.olt.racketclash.state.rule

import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.Game
import com.olt.racketclash.database.api.Rule
import com.olt.racketclash.database.api.Tournament

data class RuleState(
    val isLoading: Boolean = true,
    val rule: Rule = Rule(),
    val tournaments: List<Tournament> = emptyList(),
    val categories: List<Category> = emptyList(),
    val games: List<Game> = emptyList()
)