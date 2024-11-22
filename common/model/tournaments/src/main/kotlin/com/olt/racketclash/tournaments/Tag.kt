package com.olt.racketclash.tournaments

sealed class Tag {
    data class Name(val text: String) : Tag()
    data class Location(val text: String) : Tag()
    data class Year(val text: String) : Tag()

    fun changeText(newText: String): Tag =
        when (this) {
            is Name -> Name(text = newText)
            is Location -> Location(text = newText)
            is Year -> Year(text = newText)
        }
}