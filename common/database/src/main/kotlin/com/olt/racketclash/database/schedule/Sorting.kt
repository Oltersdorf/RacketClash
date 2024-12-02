package com.olt.racketclash.database.schedule

sealed class Sorting(val name: String) {
    data object ActiveAsc : Sorting("activeAsc")
    data object ActiveDesc : Sorting("activeDesc")
    data object ScheduleAsc : Sorting("scheduleAsc")
    data object ScheduleDesc : Sorting("scheduleDesc")
    data object TypeAsc : Sorting("typeAsc")
    data object TypeDesc : Sorting("typeDesc")
    data object CategoryAsc : Sorting("categoryAsc")
    data object CategoryDesc : Sorting("categoryDesc")
}