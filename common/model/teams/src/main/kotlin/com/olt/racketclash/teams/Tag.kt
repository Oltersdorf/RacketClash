package com.olt.racketclash.teams

sealed class Tag {
    data class Name(val text: String) : Tag()

    fun changeText(newText: String): Tag =
        when (this) {
            is Name -> Name(text = newText)
        }
}