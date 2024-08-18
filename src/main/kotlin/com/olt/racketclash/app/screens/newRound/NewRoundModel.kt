package com.olt.racketclash.app.screens.newRound

import com.olt.racketclash.data.Bye
import com.olt.racketclash.data.database.Database
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.sort
import com.olt.racketclash.state.ViewModelState

class NewRoundModel(
    private val database: Database,
    private val projectId: Long
) : ViewModelState<NewRoundModel.State>(initialState = State(projectId = projectId)) {

    private var completePlayers: List<Player> = emptyList()

    init {
        onIO {
            database.activePlayers().collect {
                updateState {
                    completePlayers = it
                    copy(players = it.filter { it.name.contains(filter) }.sort(sortedBy = sortedBy))
                }
            }
        }
    }

    sealed class RoundType {
        data object Empty : RoundType()
        data class EquallyStrongDouble(
            val rounds: Int = 1,
            val differentPartnersEachRound: Boolean = true,
            val tryUntilWorstPerformanceIsZero: Boolean = true,
            val tryUntilNoMoreThanOneByePerPerson: Boolean = true,
            val maxRepeat: Int = 10,
            val byeGames: List<Bye> = emptyList(),
            val byePlayer: String = "",
            val games: List<Game> = emptyList(),
            val performance: Int = 0
        ) : RoundType()
    }

    data class State(
        val projectId: Long,
        val canCreate: Boolean = false,
        val roundName: String = "",
        val generating: Boolean = false,
        val players: List<Player> = emptyList(),
        val roundTypes: List<RoundType> = listOf(RoundType.Empty, RoundType.EquallyStrongDouble()),
        val selectedRoundType: RoundType = RoundType.Empty,
        val filter: String = "",
        val sortedBy: Player.Sorting = Player.Sorting.NameAscending
    )

    fun changeRoundName(newName: String) =
        onDefault {
            updateState {
                if (generating) this
                else
                    copy(
                        roundName = newName,
                        canCreate = newName.isNotBlank()
                    )
            }
        }

    fun changeRoundType(newRoundType: RoundType) =
        onDefault {
            updateState {
                if (generating) this
                else copy(selectedRoundType = newRoundType)
            }
        }

    fun addEmptyRound() =
        onIO {
            database.addRound(name = state.value.roundName, projectId = projectId)
        }

    fun changeEquallyStrongDoublesRounds(newRounds: Int) =
        onDefault {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (newRounds >= 1 && roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(rounds = newRounds))
                else this
            }
        }

    fun changeEquallyStrongDoublesDifferentPartners(different: Boolean) =
        onDefault {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(differentPartnersEachRound = different))
                else this
            }
        }

    fun changeTryUntilNoMoreThanOneByePerPerson(onlyOneByPerPerson: Boolean) =
        onDefault {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(tryUntilNoMoreThanOneByePerPerson = onlyOneByPerPerson))
                else this
            }
        }

    fun changeTryUntilStrengthDifferenceIsZero(strengthDifferent: Boolean) =
        onDefault {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(tryUntilWorstPerformanceIsZero = strengthDifferent))
                else this
            }
        }

    fun changeEquallyStrongDoublesMaxRepeats(newMaxRepeats: Int) =
        onDefault {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (newMaxRepeats >= 1 && roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(maxRepeat = newMaxRepeats))
                else this
            }
        }

    fun generateEquallyStrongDoubles() =
        onDefault {
            updateState { copy(generating = true) }

            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble

                if (roundType == null) copy(generating = false)
                else {
                    val generator = EquallyStrongDoublesGenerator()
                    val (games, byes, worstPerformance) = generator.getDoubles(
                        rounds = roundType.rounds,
                        players = players.filter { it.active },
                        differentPartnersEachRound = roundType.differentPartnersEachRound,
                        tryUntilWorstPerformanceIsZero = roundType.tryUntilWorstPerformanceIsZero,
                        tryUntilNoMoreThanOneBye = roundType.tryUntilNoMoreThanOneByePerPerson,
                        maxRepeat = roundType.maxRepeat
                    )

                    val generatedBye = byes
                        .flatMap { entry ->
                            entry.value.map {
                                Bye(
                                    roundId = entry.key.toLong(),
                                    playerId = it
                                )
                            }
                        }

                    val generatedByePlayers = generatedBye
                        .groupingBy { bye ->
                            val player = players.find { it.id == bye.playerId }
                            "${player?.name} (${player?.teamName})"
                        }
                        .eachCount()
                        .flatMap { listOf("${it.value}x ${it.key}") }
                        .joinToString(separator = ", ")

                    val generatedGames = games.flatMap { entry ->
                        entry.value.map {
                            Game(
                                roundId = entry.key.toLong(),
                                playerLeft1Id = it.player1LeftId,
                                playerLeft2Id = it.player2LeftId,
                                playerRight1Id = it.player1RightId,
                                playerRight2Id = it.player2RightId
                            )
                        }
                    }

                    copy(
                        generating = false,
                        selectedRoundType = roundType.copy(
                            games = generatedGames,
                            byeGames = generatedBye,
                            byePlayer = generatedByePlayers,
                            performance = worstPerformance)
                    )
                }
            }
        }

    fun addEquallyStrongDoubles() =
        onIO {
            val model = state.value
            val roundType = model.selectedRoundType as? RoundType.EquallyStrongDouble

            if (roundType != null) {
                val games = roundType.games.groupBy { it.roundId }
                val rounds = games.mapKeys { "${model.roundName} ${it.key}" }

                database.addRoundsWithGames(rounds = rounds, bye = roundType.byeGames, projectId = projectId)
            }
        }

    fun changeFilter(newFilter: String) =
        onDefault {
            updateState {
                copy(
                    filter = newFilter,
                    players = completePlayers.filter { it.name.contains(newFilter) }.sort(sortedBy = sortedBy)
                )
            }
        }

    fun changeSorting(newSorting: Player.Sorting) =
        onDefault {
            updateState {
                copy(
                    sortedBy = newSorting,
                    players = completePlayers.filter { it.name.contains(filter) }.sort(sortedBy = newSorting)
                )
            }
        }

    fun updateActive(playerId: Long, active: Boolean) =
        onDefault {
            updateState {
                completePlayers = completePlayers
                    .toMutableList()
                    .apply { replaceAll { if (it.id == playerId) it.copy(active = active) else it } }

                copy(players = completePlayers.filter { it.name.contains(filter) }.sort(sortedBy = sortedBy))
            }
        }
}