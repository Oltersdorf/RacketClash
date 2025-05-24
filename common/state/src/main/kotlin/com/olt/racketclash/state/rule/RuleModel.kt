package com.olt.racketclash.state.rule

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.detail.DetailModel

class RuleModel(
    private val ruleDatabase: RuleDatabase,
    private val tournamentDatabase: TournamentDatabase,
    private val categoryDatabase: CategoryDatabase,
    private val gameDatabase: GameDatabase,
    private val ruleId: Long
) : DetailModel<Rule, RuleData>(
    initialItem = Rule(),
    initialData = RuleData()
) {

    override suspend fun databaseUpdate(item: Rule) =
        ruleDatabase.update(rule = item)

    override suspend fun databaseSelectItem(): Rule =
        ruleDatabase.selectSingle(id = ruleId)

    override suspend fun databaseSelectData(item: Rule): RuleData =
        RuleData(
            tournaments = tournamentDatabase.selectLast(n = 5),
            categories = categoryDatabase.selectLast(n = 5),
            games = gameDatabase.selectLast(n = 5)
        )
}