package com.olt.racketclash.data

data class Player(
    val id: Long = -1,
    val active: Boolean = false,
    val name: String = "",
    val teamId: Long = -1,
    val teamName: String = "",
    val teamStrength: Int = 0,
    val openGames: Int = 0,
    val played: Int = 0,
    val bye: Int = 0,
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

    companion object {
        fun sortingOptions() = listOf(
            Sorting.NameAscending, Sorting.NameDescending,
            Sorting.TeamAscending, Sorting.TeamDescending,
            Sorting.PointsAscending, Sorting.PointsDescending,
            Sorting.PendingAscending, Sorting.PendingDescending,
            Sorting.PlayedAscending, Sorting.PlayedDescending,
            Sorting.ByeAscending, Sorting.ByeDescending
        )
    }
}

fun List<Player>.sort(sortedBy: Player.Sorting): List<Player> =
    when (sortedBy) {
        Player.Sorting.NameAscending -> sortedBy { it.name }
        Player.Sorting.NameDescending -> sortedByDescending { it.name }
        Player.Sorting.PointsAscending -> sortedWith(compareBy(Player::wonGames, Player::lostGames, Player::wonSets, Player::lostSets, Player::wonPoints, Player::lostPoints))
        Player.Sorting.PointsDescending -> sortedWith(compareBy(Player::wonGames, Player::lostGames, Player::wonSets, Player::lostSets, Player::wonPoints, Player::lostPoints).reversed())
        Player.Sorting.TeamAscending -> sortedBy { it.teamName }
        Player.Sorting.TeamDescending -> sortedByDescending { it.teamName }
        Player.Sorting.ByeAscending -> sortedBy { it.bye }
        Player.Sorting.ByeDescending -> sortedByDescending { it.bye }
        Player.Sorting.PendingAscending -> sortedBy { it.openGames }
        Player.Sorting.PendingDescending -> sortedByDescending { it.openGames }
        Player.Sorting.PlayedAscending -> sortedBy { it.played }
        Player.Sorting.PlayedDescending -> sortedByDescending { it.played }
    }

