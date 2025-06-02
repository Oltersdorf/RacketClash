package com.olt.racketclash.state.location

import com.olt.racketclash.database.api.Location
import com.olt.racketclash.database.api.LocationDatabase
import com.olt.racketclash.database.api.TournamentDatabase
import com.olt.racketclash.state.detail.DetailModel

class LocationModel(
    private val locationDatabase: LocationDatabase,
    private val tournamentDatabase: TournamentDatabase,
    private val locationId: Long
) : DetailModel<Location, LocationData>(
    initialItem = Location(),
    initialData = LocationData()
) {

    override suspend fun databaseSelectItem(): Location =
        locationDatabase.selectSingle(id = locationId)

    override suspend fun databaseUpdate(item: Location) =
        locationDatabase.update(location = item)

    override suspend fun databaseSelectData(item: Location): LocationData =
        LocationData(
            tournaments = tournamentDatabase.selectLast(n = 5)
        )
}