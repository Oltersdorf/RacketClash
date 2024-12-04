package com.olt.racketclash.addschedule

data class State(
    val isLoading: Boolean = false,
    val isSavable: Boolean = false,
    val isGenerating: Boolean = false,
    val rounds: Int = 1,
    val differentPartnersEachRound: Boolean = true,
    val onlyOneRestPerPlayer: Boolean = true,
    val worstStrengthDifferenceIsZero: Boolean = true,
    val maxRepeats: Int = 1,
    val players: List<SelectablePlayer> = listOf(
        SelectablePlayer(id = 0L, selected = false, name = "player 1", team = "team 1"),
        SelectablePlayer(id = 1L, selected = false, name = "player 2", team = "team 2"),
        SelectablePlayer(id = 2L, selected = false, name = "player 3", team = "team 2"),
        SelectablePlayer(id = 3L, selected = false, name = "player 4", team = "team 2"),
        SelectablePlayer(id = 4L, selected = false, name = "player 5", team = "team 2")
    ),
    val selectedPlayers: List<Long> = emptyList(),
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val generatedGames: List<EquallyStrongDoublesGenerator.SimpleGame> = emptyList(),
    val generatedRests: List<Player> = emptyList(),
    val generatedPerformance: Int = 0
)