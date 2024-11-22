package com.olt.racketclash.player

sealed class Tag {
    data class Date(val text: String) : Tag()
    data class Tournament(val text: String) : Tag()
    data class Category(val text: String) : Tag()
    data class Rule(val text: String) : Tag()
    data class Player(val text: String) : Tag()

    fun changeText(newText: String): Tag =
        when (this) {
            is Date -> Date(text = newText)
            is Category -> Category(text = newText)
            is Rule -> Rule(text = newText)
            is Player -> Player(text = newText)
            is Tournament -> Tournament(text = newText)
        }
}