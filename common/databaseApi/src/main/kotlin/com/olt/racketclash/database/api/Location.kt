package com.olt.racketclash.database.api

data class LocationFilter(
    val name: String = "",
    val address: String = "",
    val postcode: String = "",
    val city: String = "",
    val country: String = ""
)

sealed interface LocationSorting {
    data object NameAsc : LocationSorting
    data object NameDesc : LocationSorting
    data object AddressAsc : LocationSorting
    data object AddressDesc : LocationSorting
    data object PostcodeAsc : LocationSorting
    data object PostcodeDesc : LocationSorting
    data object CityAsc : LocationSorting
    data object CityDesc : LocationSorting
    data object CountryAsc : LocationSorting
    data object CountryDesc : LocationSorting
}

data class Location(
    val id: Long = -1,
    val name: String = "",
    val address: String = "",
    val postcode: String = "",
    val city: String = "",
    val country: String = "",
    val gpsCoordinates: String = ""
): Validateable {

    override fun validate(): Boolean {
        return name.isNotBlank() &&
                address.isNotBlank() &&
                postcode.isNotBlank() &&
                city.isNotBlank() &&
                country.isNotBlank() &&
                gpsCoordinates.isNotBlank()
    }
}

interface LocationDatabase {
    suspend fun selectList(
        filter: LocationFilter,
        sorting: LocationSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Location, LocationFilter, LocationSorting>

    suspend fun selectLast(n: Long): List<Location>

    suspend fun selectSingle(id: Long): Location

    suspend fun add(location: Location)

    suspend fun update(location: Location)

    suspend fun delete(id: Long)
}