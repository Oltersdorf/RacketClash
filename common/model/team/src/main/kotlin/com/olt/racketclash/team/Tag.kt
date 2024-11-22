package com.olt.racketclash.team

sealed class Tag {
    data class Name(val text: String) : Tag()
    data class BirthYear(val text: String) : Tag()
    data class Club(val text: String) : Tag()

    fun changeText(newText: String): Tag =
        when (this) {
            is Name -> Name(text = newText)
            is BirthYear -> BirthYear(text = newText)
            is Club -> Club(text = newText)
        }
}