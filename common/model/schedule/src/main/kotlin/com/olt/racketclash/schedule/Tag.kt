package com.olt.racketclash.schedule

sealed class Tag {
    data object Active : Tag()
    data object Singles : Tag()
    data object Doubles : Tag()
    data class Category(val text: String) : Tag()
    data class Player(val text: String) : Tag()

    fun changeText(newText: String): Tag =
        when (this) {
            is Active -> this
            is Category -> Category(text = newText)
            Doubles -> this
            is Player -> Player(text = newText)
            Singles -> this
        }
}