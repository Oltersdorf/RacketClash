package com.olt.racketclash.data

data class Team(
    val id: Long,
    val name: String,
    val strength: Int,
    val size: Int,
    val openGames: Int = 0,
    val bye: Int = 0,
    val played: Int = 0,
    val wonGames: Int = 0,
    val lostGames: Int = 0,
    val wonSets: Int = 0,
    val lostSets: Int = 0,
    val wonPoints: Int = 0,
    val lostPoints: Int = 0
) {
    sealed class Sorting {
        data object NameAscending : Sorting()
        data object NameDescending : Sorting()
        data object StrengthAscending : Sorting()
        data object StrengthDescending : Sorting()
        data object PointsAscending : Sorting()
        data object PointsDescending : Sorting()
    }

    companion object {
        fun sortingOptions() = listOf(
            Sorting.NameAscending, Sorting.NameDescending,
            Sorting.StrengthAscending, Sorting.StrengthDescending,
            Sorting.PointsAscending, Sorting.PointsDescending
        )
    }
}

fun List<Team>.sort(sortedBy: Team.Sorting): List<Team> =
    when (sortedBy) {
        Team.Sorting.NameAscending -> sortedBy { it.name }
        Team.Sorting.NameDescending -> sortedByDescending { it.name }
        Team.Sorting.PointsAscending -> sortedWith(compareBy(Team::wonGames, Team::lostGames, Team::wonSets, Team::lostSets, Team::wonPoints, Team::lostPoints))
        Team.Sorting.PointsDescending -> sortedWith(compareBy(Team::wonGames, Team::lostGames, Team::wonSets, Team::lostSets, Team::wonPoints, Team::lostPoints).reversed())
        Team.Sorting.StrengthAscending -> sortedBy { it.strength }
        Team.Sorting.StrengthDescending -> sortedByDescending { it.strength }
    }
