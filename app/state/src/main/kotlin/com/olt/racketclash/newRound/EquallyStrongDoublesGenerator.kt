package com.olt.racketclash.newRound

import com.olt.racketclash.data.Player
import kotlin.math.abs
import kotlin.random.Random

class EquallyStrongDoublesGenerator(
    private val seed: Int = Random.nextInt(),
    private val bruteForceDepth: Int = 20_000
) {

    private data class SeedPerformance(
        val seed: Int,
        val performance: Int,
        val worstPerformance: Int
    )

    data class SimpleGame(
        val player1LeftId: Long,
        val player1LeftStrength: Int,
        val player2LeftId: Long,
        val player2LeftStrength: Int,
        val player1RightId: Long,
        val player1RightStrength: Int,
        val player2RightId: Long,
        val player2RightStrength: Int,
    )

    //returns games to bye to the worst performance
    fun getDoubles(
        rounds: Int,
        players: List<Player>,
        differentPartnersEachRound: Boolean,
        tryUntilWorstPerformanceIsZero: Boolean,
        tryUntilNoMoreThanOneBye: Boolean,
        maxRepeat: Int = 1
    ) : Triple<Map<Int, List<SimpleGame>>, Map<Int, List<Long>>, Int> {
        val randomGenerator = Random(seed = seed)

        var forbiddenPartners = players.associate { it.id to mutableListOf<Long>() }
        var bye = mutableMapOf<Int, List<Long>>()
        var totalGames = mutableMapOf<Int, List<SimpleGame>>()
        var worstPerformance = 0

        var repeat: Boolean
        var counter = 1
        do {
            for (i in 1..rounds) {
                val bestPerformance = bestPerformance(
                    players = players,
                    forbiddenPartners = if (differentPartnersEachRound) forbiddenPartners else emptyMap(),
                    seed = randomGenerator.nextInt()
                )
                val games = generateGames(
                    players = players,
                    seed = Random(bestPerformance.seed),
                    forbiddenPartners = if (differentPartnersEachRound) forbiddenPartners else emptyMap()
                )

                val potentiallyBye = players.map { it.id }.toMutableList()
                for (g in games) {
                    forbiddenPartners[g.player1LeftId]?.add(g.player2LeftId)
                    forbiddenPartners[g.player2LeftId]?.add(g.player1LeftId)
                    forbiddenPartners[g.player1RightId]?.add(g.player2RightId)
                    forbiddenPartners[g.player2RightId]?.add(g.player1RightId)
                    potentiallyBye.removeAll(listOf(g.player1LeftId, g.player2LeftId, g.player1RightId, g.player2RightId))
                }

                if (bestPerformance.worstPerformance > worstPerformance) worstPerformance = bestPerformance.worstPerformance
                totalGames[i] = games
                bye[i] = potentiallyBye
            }

            //check for repeating
            var repeatPerformance = tryUntilWorstPerformanceIsZero
            if (tryUntilWorstPerformanceIsZero && worstPerformance == 0)
                repeatPerformance = false
            var repeatBye = tryUntilNoMoreThanOneBye
            val byeIds = bye.values.flatten()
            if (tryUntilNoMoreThanOneBye && byeIds.size == byeIds.toSet().size)
                repeatBye = false

            repeat = repeatPerformance || repeatBye
            counter += 1

            //reset if in case of repeat
            if (repeat && counter <= maxRepeat) {
                forbiddenPartners = players.associate { it.id to mutableListOf() }
                bye = mutableMapOf()
                totalGames = mutableMapOf()
                worstPerformance = 0
            }
        } while (repeat && counter <= maxRepeat)

        return Triple(totalGames.toMap(), bye.toMap(), worstPerformance)
    }

    private fun generateGames(
        players: List<Player>,
        seed: Random,
        forbiddenPartners: Map<Long, List<Long>>
    ) : List<SimpleGame> {
        val availablePlayers = players.toMutableList()
        val games = mutableListOf<SimpleGame>()

        //generate (player / 4) games
        for (i in 1..(players.size / 4)) {
            //select random player
            val p1Left = availablePlayers.random(seed)
            availablePlayers.remove(p1Left)

            //find valid partners for p1Left
            val p1LeftValidPartners = availablePlayers.filterNot { forbiddenPartners[p1Left.id]?.contains(it.id) ?: false }
            if (p1LeftValidPartners.isEmpty()) return emptyList()
            //select partner for p1Left
            val p2Left = p1LeftValidPartners.random(seed)
            availablePlayers.remove(p2Left)

            //select random player
            val p1Right = availablePlayers.random(seed)
            availablePlayers.remove(p1Right)

            //find valid partner for p1Right
            val p1RightValidPartners = availablePlayers.filterNot { forbiddenPartners[p1Right.id]?.contains(it.id) ?: false }
            if (p1RightValidPartners.isEmpty()) return emptyList()
            //select partner for p1Right
            val p2Right = p1RightValidPartners.random(seed)
            availablePlayers.remove(p2Right)

            games.add(
                SimpleGame(
                    player1LeftId = p1Left.id,
                    player1LeftStrength = p1Left.teamStrength,
                    player2LeftId = p2Left.id,
                    player2LeftStrength = p2Left.teamStrength,
                    player1RightId = p1Right.id,
                    player1RightStrength = p1Right.teamStrength,
                    player2RightId = p2Right.id,
                    player2RightStrength = p2Right.teamStrength
                )
            )
        }

        return games.toList()
    }

    private fun bestPerformance(
        players: List<Player>,
        forbiddenPartners: Map<Long, List<Long>>,
        seed: Int
    ) : SeedPerformance {
        val seedPerformance = mutableListOf<SeedPerformance>()
        val randomizer = Random(seed = seed)

        for (i in 1..bruteForceDepth) {
            val currentSeed = randomizer.nextInt()
            val gamesGenerated = generateGames(players = players, seed = Random(seed = currentSeed), forbiddenPartners = forbiddenPartners)

            val (performance, worstPerformance) = evaluateGames(games = gamesGenerated)
            if (gamesGenerated.isNotEmpty())
                seedPerformance.add(
                    SeedPerformance(seed = currentSeed, performance = performance, worstPerformance = worstPerformance)
                )
        }



        return findBestPerformance(performances = seedPerformance)
    }

    private fun evaluateGames(games: List<SimpleGame>) : Pair<Int, Int> {
        var totalPerformance = 0
        var worstPerformance = 0

        games.forEach {
            val performance = abs(
                (it.player1LeftStrength + it.player2LeftStrength) -
                        (it.player1RightStrength + it.player2RightStrength)
            )
            totalPerformance += performance
            if (performance > worstPerformance)
                worstPerformance = performance
        }

        return totalPerformance to worstPerformance
    }

    private fun findBestPerformance(performances: List<SeedPerformance>) : SeedPerformance {
        var bestPerformance = SeedPerformance(seed = -1, performance = Int.MAX_VALUE, worstPerformance = Int.MAX_VALUE)

        for (p in performances) {
            if (p.worstPerformance < bestPerformance.worstPerformance)
                bestPerformance = p
            else if (p.worstPerformance == bestPerformance.worstPerformance && p.performance < bestPerformance.performance)
                bestPerformance = p
        }

        return bestPerformance
    }
}