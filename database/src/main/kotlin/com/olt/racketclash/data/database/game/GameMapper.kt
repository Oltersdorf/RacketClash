package com.olt.racketclash.data.database.game

import com.olt.racketclash.data.Game
import com.olt.racketclash.database.game.SelectAll
import com.olt.racketclash.database.game.SelectAllInRound

internal fun SelectAll.toGame() =
    Game(
        id = id,
        roundId = roundId,
        isDone = isDone,
        playerLeft1Id = playerLeft1Id,
        playerLeft1Name = playerLeft1Name,
        playerLeft1TeamName = playerLeft1TeamName,
        playerLeft1LastPlayed = playerLeft1LastPlayed,
        playerLeft2Id = playerLeft2Id,
        playerLeft2Name = playerLeft2Name,
        playerLeft2TeamName = playerLeft2TeamName,
        playerLeft2LastPlayed = playerLeft2LastPlayed,
        playerRight1Id = playerRight1Id,
        playerRight1Name = playerRight1Name,
        playerRight1TeamName = playerRight1TeamName,
        playerRight1LastPlayed = playerRight1LastPlayed,
        playerRight2Id = playerRight2Id,
        playerRight2Name = playerRight2Name,
        playerRight2TeamName = playerRight2TeamName,
        playerRight2LastPlayed = playerRight2LastPlayed,
        set1Left = set1Left,
        set1Right = set1Right,
        set2Left = set2Left,
        set2Right = set2Right,
        set3Left = set3Left,
        set3Right = set3Right,
        set4Left = set4Left,
        set4Right = set4Right,
        set5Left = set5Left,
        set5Right = set5Right
    )

internal fun SelectAllInRound.toGame() =
    Game(
        id = id,
        roundId = roundId,
        isDone = isDone,
        playerLeft1Id = playerLeft1Id,
        playerLeft1Name = playerLeft1Name,
        playerLeft1TeamName = playerLeft1TeamName,
        playerLeft2Id = playerLeft2Id,
        playerLeft2Name = playerLeft2Name,
        playerLeft2TeamName = playerLeft2TeamName,
        playerRight1Id = playerRight1Id,
        playerRight1Name = playerRight1Name,
        playerRight1TeamName = playerRight1TeamName,
        playerRight2Id = playerRight2Id,
        playerRight2Name = playerRight2Name,
        playerRight2TeamName = playerRight2TeamName,
        set1Left = set1Left,
        set1Right = set1Right,
        set2Left = set2Left,
        set2Right = set2Right,
        set3Left = set3Left,
        set3Right = set3Right,
        set4Left = set4Left,
        set4Right = set4Right,
        set5Left = set5Left,
        set5Right = set5Right
    )