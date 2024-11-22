package com.olt.racketclash.players

sealed class Tag {
    data class Name(val text: String) : Tag()
    data class BirthYear(val text: String) : Tag()
    data class Club(val text: String) : Tag()
    data object HasMedals : Tag()
    data object HasNoMedals : Tag()

    fun changeText(newText: String): Tag =
        when (this) {
            is Name -> Name(text = newText)
            is BirthYear -> BirthYear(text = newText)
            is Club -> Club(text = newText)
            HasMedals -> this
            HasNoMedals -> this
        }
}