package com.olt.racketclash.screens.newRound

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.database.Database
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Player
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewRoundModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<NewRoundModel.Model>(navigateToScreen, Model()) {

    private var completePlayers: List<Player> = emptyList()

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.activePlayers().collect {
                updateState {
                    completePlayers = it
                    copy(players = it.sortAndFilter(filter = filter, sortedBy = sortedBy))
                }
            }
        }
    }

    sealed class Sorting {
        data object NameAscending : Sorting()
        data object NameDescending : Sorting()
        data object TeamAscending : Sorting()
        data object TeamDescending : Sorting()
        data object PointsAscending : Sorting()
        data object PointsDescending : Sorting()
        data object PendingAscending : Sorting()
        data object PendingDescending : Sorting()
        data object ByeAscending : Sorting()
        data object ByeDescending : Sorting()
        data object PlayedAscending : Sorting()
        data object PlayedDescending : Sorting()
    }

    sealed class RoundType {
        data object Empty : RoundType()
        data class EquallyStrongDouble(
            val rounds: Int = 1,
            val differentPartnersEachRound: Boolean = true,
            val tryUntilWorstPerformanceIsZero: Boolean = true,
            val tryUntilNoMoreThanOneByePerPerson: Boolean = true,
            val maxRepeat: Int = 10,
            val byeGames: List<Game> = emptyList(),
            val byePlayer: String = "",
            val games: List<Game> = emptyList(),
            val performance: Int = 0
        ) : RoundType()
    }

    data class Model(
        val canCreate: Boolean = false,
        val roundName: String = "",
        val generating: Boolean = false,
        val players: List<Player> = emptyList(),
        val roundTypes: List<RoundType> = listOf(RoundType.Empty, RoundType.EquallyStrongDouble()),
        val selectedRoundType: RoundType = RoundType.Empty,
        val filter: String = "",
        val availableSorting: List<Sorting> =
            listOf(
                Sorting.NameAscending, Sorting.NameDescending,
                Sorting.TeamAscending, Sorting.TeamDescending,
                Sorting.PointsAscending, Sorting.PointsDescending,
                Sorting.PendingAscending, Sorting.PendingDescending,
                Sorting.PlayedAscending, Sorting.PlayedDescending,
                Sorting.ByeAscending, Sorting.ByeDescending
            ),
        val sortedBy: Sorting = Sorting.NameAscending
    )

    fun changeRoundName(newName: String) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                if (generating) this
                else
                    copy(
                        roundName = newName,
                        canCreate = newName.isNotBlank()
                    )
            }
        }
    }

    fun changeRoundType(newRoundType: RoundType) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                if (generating) this
                else copy(selectedRoundType = newRoundType)
            }
        }
    }

    fun addEmptyRound() {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.addRound(name = mutableState.value.roundName)
        }
    }

    fun changeEquallyStrongDoublesRounds(newRounds: Int) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (newRounds >= 1 && roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(rounds = newRounds))
                else this
            }
        }
    }

    fun changeEquallyStrongDoublesDifferentPartners(different: Boolean) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(differentPartnersEachRound = different))
                else this
            }
        }
    }

    fun changeTryUntilNoMoreThanOneByePerPerson(onlyOneByPerPerson: Boolean) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(tryUntilNoMoreThanOneByePerPerson = onlyOneByPerPerson))
                else this
            }
        }
    }

    fun changeTryUntilStrengthDifferenceIsZero(strengthDifferent: Boolean) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(tryUntilWorstPerformanceIsZero = strengthDifferent))
                else this
            }
        }
    }

    fun changeEquallyStrongDoublesMaxRepeats(newMaxRepeats: Int) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble
                if (newMaxRepeats >= 1 && roundType != null && !generating)
                    copy(selectedRoundType = roundType.copy(maxRepeat = newMaxRepeats))
                else this
            }
        }
    }

    fun generateEquallyStrongDoubles() {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState { copy(generating = true) }

            updateState {
                val roundType = selectedRoundType as? RoundType.EquallyStrongDouble

                if (roundType == null) copy(generating = false)
                else {
                    val generator = EquallyStrongDoublesGenerator()
                    val (games, bye, worstPerformance) = generator.getDoubles(
                        rounds = roundType.rounds,
                        players = players.filter { it.active },
                        differentPartnersEachRound = roundType.differentPartnersEachRound,
                        tryUntilWorstPerformanceIsZero = roundType.tryUntilWorstPerformanceIsZero,
                        tryUntilNoMoreThanOneBye = roundType.tryUntilNoMoreThanOneByePerPerson,
                        maxRepeat = roundType.maxRepeat
                    )

                    val generatedBye = bye
                        .flatMap { entry ->
                            entry.value.map {
                                Game(
                                    roundId = entry.key.toLong(),
                                    playerLeft1Id = it
                                )
                            }
                        }

                    val generatedByePlayers = generatedBye
                        .groupingBy { game ->
                            val player = players.find { it.id == game.playerLeft1Id }
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
    }

    fun addEquallyStrongDoubles() {
        screenModelScope.launch(context = Dispatchers.IO) {
            val model = mutableState.value
            val roundType = model.selectedRoundType as? RoundType.EquallyStrongDouble

            if (roundType != null) {
                val games = roundType.games.groupBy { it.roundId }
                val rounds = games.mapKeys { "${model.roundName} ${it.key}" }

                database.addRoundsWithGames(rounds = rounds, bye = roundType.byeGames)
            }
        }
    }

    fun changeFilter(newFilter: String) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    filter = newFilter,
                    players = completePlayers.sortAndFilter(filter = newFilter, sortedBy = sortedBy)
                )
            }
        }
    }

    fun changeSorting(newSorting: Sorting) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    sortedBy = newSorting,
                    players = completePlayers.sortAndFilter(filter = filter, sortedBy = newSorting)
                )
            }
        }
    }

    fun updateActive(playerId: Long, active: Boolean) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                 completePlayers = completePlayers
                     .toMutableList()
                     .apply { replaceAll { if (it.id == playerId) it.copy(active = active) else it } }

                copy(players = completePlayers.sortAndFilter(filter = filter, sortedBy = sortedBy))
            }
        }
    }

    private fun List<Player>.sortAndFilter(filter: String, sortedBy: Sorting): List<Player> {
        val teams = filter { it.name.contains(filter) }

        return when (sortedBy) {
            Sorting.NameAscending -> teams.sortedBy { it.name }
            Sorting.NameDescending -> teams.sortedByDescending { it.name }
            Sorting.PointsAscending -> teams.sortedWith(compareBy(Player::wonGames, Player::lostGames, Player::wonSets, Player::lostSets, Player::wonPoints, Player::lostPoints))
            Sorting.PointsDescending -> teams.sortedWith(compareBy(Player::wonGames, Player::lostGames, Player::wonSets, Player::lostSets, Player::wonPoints, Player::lostPoints).reversed())
            Sorting.TeamAscending -> teams.sortedBy { it.teamName }
            Sorting.TeamDescending -> teams.sortedByDescending { it.teamName }
            Sorting.ByeAscending -> teams.sortedBy { it.bye }
            Sorting.ByeDescending -> teams.sortedByDescending { it.bye }
            Sorting.PendingAscending -> teams.sortedBy { it.openGames }
            Sorting.PendingDescending -> teams.sortedByDescending { it.openGames }
            Sorting.PlayedAscending -> teams.sortedBy { it.played }
            Sorting.PlayedDescending -> teams.sortedByDescending { it.played }
        }
    }
}