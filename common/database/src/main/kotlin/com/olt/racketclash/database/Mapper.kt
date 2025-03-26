package com.olt.racketclash.database

import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.table.FilteredAndOrderedPlayer
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.database.table.FilteredAndOrderedTeam
import com.olt.racketclash.database.table.category.SelectFilteredAndOrdered
import com.olt.racketclash.database.table.category.SelectSingle
import com.olt.racketclash.database.table.player.SelectLast
import com.olt.racketclash.database.view.SelectGameSchedule
import java.time.Instant

internal fun SelectGameSchedule.toGame(
    sets: List<GameSet>
): Game =
    Game(
        id = gameId,
        scheduleId = scheduleId,
        scheduled = Instant.ofEpochSecond(unixTimeScheduled),
        submitted = unixTimeSubmitted?.let { Instant.ofEpochSecond(it) },
        playerIdLeftOne = playerIdLeftOne,
        playerNameLeftOne = playerNameLeftOne!!,
        playerTeamIdLeftOne = playerTeamIdLeftOne,
        playerTeamNameLeftOne = playerTeamNameLeftOne,
        playerIdLeftTwo = playerIdLeftTwo,
        playerNameLeftTwo = playerNameLeftTwo,
        playerTeamIdLeftTwo = playerTeamIdLeftTwo,
        playerTeamNameLeftTwo = playerTeamNameLeftTwo,
        playerIdRightOne = playerIdRightOne,
        playerNameRightOne = playerNameRightOne,
        playerTeamIdRightOne = playerTeamIdRightOne,
        playerTeamNameRightOne = playerTeamNameRightOne,
        playerIdRightTwo = playerIdRightTwo,
        playerNameRightTwo = playerNameRightTwo,
        playerTeamIdRightTwo = playerTeamIdRightTwo,
        playerTeamNameRightTwo = playerTeamNameRightTwo,
        sets = sets
    )

internal fun com.olt.racketclash.database.table.GameSet.toGameSet(): GameSet =
    GameSet(
        id = id,
        gameId = gameId,
        orderNumber = orderNumber,
        leftPoints = leftPoints,
        rightPoints = rightPoints
    )

internal fun SelectFilteredAndOrdered.toCategory(): Category =
    Category(
        id = id,
        name = name,
        type = type,
        tournamentId = tournamentId,
        players = players.toInt(),
        finished = false
    )

internal fun SelectSingle.toCategory(): Category =
    Category(
        id = id,
        name = name,
        type = type,
        tournamentId = tournamentId,
        players = 0,
        finished = false
    )

internal fun CategorySorting.toName(): String =
    when (this) {
        CategorySorting.NameAsc -> "nameAsc"
        CategorySorting.NameDesc -> "nameDesc"
        CategorySorting.PlayersAsc -> "playersAsc"
        CategorySorting.PlayersDesc -> "playersDesc"
        CategorySorting.StatusAsc -> "statusAsc"
        CategorySorting.StatusDesc -> "statusDesc"
        CategorySorting.TypeAsc -> "typeAsc"
        CategorySorting.TypeDesc -> "typeDesc"
    }

internal fun SelectLast.toPlayer(): Player =
    Player(
        id = id,
        name = name,
        birthYear = birthYear,
        club = club,
        numberOfTournaments = numberOfTournaments,
        goldMedals = goldMedals,
        silverMedals = silverMedals,
        bronzeMedals = bronzeMedals,
        gamesPlayed = gamesPlayed,
        gamesScheduled = gamesScheduled
    )

internal fun com.olt.racketclash.database.table.Player.toPlayer(): Player =
    Player(
        id = id,
        name = name,
        birthYear = birthYear,
        club = club,
        numberOfTournaments = numberOfTournaments,
        goldMedals = goldMedals,
        silverMedals = silverMedals,
        bronzeMedals = bronzeMedals,
        gamesPlayed = gamesPlayed,
        gamesScheduled = gamesScheduled
    )

internal fun FilteredAndOrderedPlayer.toPlayer(): Player =
    Player(
        id = id,
        name = name,
        birthYear = birthYear,
        club = club,
        numberOfTournaments = numberOfTournaments,
        goldMedals = goldMedals,
        silverMedals = silverMedals,
        bronzeMedals = bronzeMedals,
        gamesPlayed = gamesPlayed,
        gamesScheduled = gamesScheduled
    )

internal fun PlayerSorting.toName(): String =
    when (this) {
        PlayerSorting.BirthYearAsc -> "birthYearAsc"
        PlayerSorting.BirthYearDesc -> "birthYearDesc"
        PlayerSorting.ClubAsc -> "clubAsc"
        PlayerSorting.ClubDesc -> "clubDesc"
        PlayerSorting.DoublesAsc -> "doublesAsc"
        PlayerSorting.DoublesDesc -> "doublesDesc"
        PlayerSorting.MedalsAsc -> "medalsAsc"
        PlayerSorting.MedalsDesc -> "medalsDesc"
        PlayerSorting.NameAsc -> "nameAsc"
        PlayerSorting.NameDesc -> "nameDesc"
        PlayerSorting.SinglesAsc -> "singlesAsc"
        PlayerSorting.SinglesDesc -> "singlesDesc"
        PlayerSorting.TournamentsAsc -> "tournamentsAsc"
        PlayerSorting.TournamentsDesc -> "tournamentsDesc"
    }

internal fun com.olt.racketclash.database.table.Rule.toRule() : Rule =
    Rule(
        id = id,
        name = name,
        maxSets = maxSets,
        winSets = winSets,
        maxPoints = maxPoints,
        winPoints = winPoints,
        pointsDifference = pointsDifference,
        gamePointsForWin = gamePointsForWin,
        gamePointsForLose = gamePointsForLose,
        gamePointsForDraw = gamePointsForDraw,
        gamePointsForRest = gamePointsForRest,
        setPointsForRest = setPointsForRest,
        pointPointsForRest = pointPointsForRest,
        used = used
    )

internal fun FilteredAndOrderedRule.toRule() : Rule =
    Rule(
        id = id,
        name = name,
        maxSets = maxSets,
        winSets = winSets,
        maxPoints = maxPoints,
        winPoints = winPoints,
        pointsDifference = pointsDifference,
        gamePointsForWin = gamePointsForWin,
        gamePointsForLose = gamePointsForLose,
        gamePointsForDraw = gamePointsForDraw,
        gamePointsForRest = gamePointsForRest,
        setPointsForRest = setPointsForRest,
        pointPointsForRest = pointPointsForRest,
        used = used
    )

internal fun com.olt.racketclash.database.table.rule.SelectLast.toRule() : Rule =
    Rule(
        id = id,
        name = name,
        maxSets = maxSets,
        winSets = winSets,
        maxPoints = maxPoints,
        winPoints = winPoints,
        pointsDifference = pointsDifference,
        gamePointsForWin = gamePointsForWin,
        gamePointsForLose = gamePointsForLose,
        gamePointsForDraw = gamePointsForDraw,
        gamePointsForRest = gamePointsForRest,
        setPointsForRest = setPointsForRest,
        pointPointsForRest = pointPointsForRest,
        used = used
    )

internal fun RuleSorting.toName(): String =
    when (this) {
        RuleSorting.NameAsc -> "nameAsc"
        RuleSorting.NameDesc -> "nameDesc"
    }

internal fun com.olt.racketclash.database.table.schedule.SelectFilteredAndOrdered.toSchedule(): Schedule =
    Schedule(
        id = id,
        ruleId = ruleId,
        ruleName = ruleName ?: "",
        maxSets = maxSets ?: 1,
        scheduledFor = Instant.ofEpochSecond(unixTimeScheduled),
        active = active,
        categoryId = categoryId,
        categoryName = categoryName ?: "",
        categoryOrderNumber = categoryOrderNumber,
        tournamentId = tournamentId,
        playerIdLeftOne = playerIdLeftOne,
        playerNameLeftOne = playerNameLeftOne ?: "",
        playerIdLeftTwo = playerIdLeftTwo,
        playerNameLeftTwo = playerNameLeftTwo,
        playerIdRightOne = playerIdRightOne,
        playerNameRightOne = playerNameRightOne ?: "",
        playerIdRightTwo = playerIdRightTwo,
        playerNameRightTwo = playerNameRightTwo
    )

internal fun ScheduleSorting.toName(): String =
    when (this) {
        ScheduleSorting.ActiveAsc -> "activeAsc"
        ScheduleSorting.ActiveDesc -> "activeDesc"
        ScheduleSorting.CategoryAsc -> "categoryAsc"
        ScheduleSorting.CategoryDesc -> "categoryDesc"
        ScheduleSorting.ScheduleAsc -> "scheduleAsc"
        ScheduleSorting.ScheduleDesc -> "scheduleDesc"
        ScheduleSorting.TypeAsc -> "typeAsc"
        ScheduleSorting.TypeDesc -> "typeDesc"
    }

internal fun com.olt.racketclash.database.table.Team.toTeam(): Team =
    Team(
        id = id,
        name = name,
        rank = rank,
        tournamentId = tournamentId,
        size = size,
        gamesWon = gamesWon,
        gamesDraw = gamesDraw,
        gamesLost = gamesLost,
        gamePointsWon = gamePointsWon,
        gamePointsLost = gamePointsLost
    )

internal fun FilteredAndOrderedTeam.toTeam(): Team =
    Team(
        id = id,
        name = name,
        rank = rank,
        tournamentId = tournamentId,
        size = size,
        gamesWon = gamesWon,
        gamesDraw = gamesDraw,
        gamesLost = gamesLost,
        gamePointsWon = gamePointsWon,
        gamePointsLost = gamePointsLost
    )

internal fun TeamSorting.toName(): String =
    when (this) {
        TeamSorting.NameAsc -> "nameAsc"
        TeamSorting.NameDesc -> "nameDesc"
        TeamSorting.PointsAsc -> "pointsAsc"
        TeamSorting.PointsDesc -> "pointsDesc"
        TeamSorting.RankAsc -> "rankAsc"
        TeamSorting.RankDesc -> "rankDesc"
        TeamSorting.SizeAsc -> "sizeAsc"
        TeamSorting.SizeDesc -> "sizeDesc"
    }

internal fun com.olt.racketclash.database.table.tournament.SelectFilteredAndOrdered.toTournament(): Tournament =
    Tournament(
        id = id,
        name = name,
        numberOfCourts = numberOfCourts,
        location = location,
        start = Instant.ofEpochSecond(startTime),
        end = Instant.ofEpochSecond(endTime),
        playersCount = playersCount,
        categoriesCount = categoriesCount
    )

internal fun com.olt.racketclash.database.table.tournament.SelectLast.toTournament(): Tournament =
    Tournament(
        id = id,
        name = name,
        numberOfCourts = numberOfCourts,
        location = location,
        start = Instant.ofEpochSecond(startTime),
        end = Instant.ofEpochSecond(endTime),
        playersCount = playersCount,
        categoriesCount = categoriesCount
    )

internal fun com.olt.racketclash.database.table.tournament.SelectSingle.toTournament(): Tournament =
    Tournament(
        id = id,
        name = name,
        numberOfCourts = numberOfCourts,
        location = location,
        start = Instant.ofEpochSecond(startTime),
        end = Instant.ofEpochSecond(endTime),
        playersCount = playersCount,
        categoriesCount = categoriesCount
    )

internal fun TournamentSorting.toName(): String =
    when (this) {
        TournamentSorting.CourtsAsc -> "courtsAsc"
        TournamentSorting.CourtsDesc -> "courtsDesc"
        TournamentSorting.EndAsc -> "endAsc"
        TournamentSorting.EndDesc -> "endDesc"
        TournamentSorting.LocationAsc -> "locationAsc"
        TournamentSorting.LocationDesc -> "locationDesc"
        TournamentSorting.NameAsc -> "nameAsc"
        TournamentSorting.NameDesc -> "nameDesc"
        TournamentSorting.PlayersAsc -> "playersAsc"
        TournamentSorting.PlayersDesc -> "playersDesc"
        TournamentSorting.StartAsc -> "startAsc"
        TournamentSorting.StartDesc -> "startDesc"
    }