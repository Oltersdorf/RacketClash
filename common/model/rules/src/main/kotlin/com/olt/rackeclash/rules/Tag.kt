package com.olt.rackeclash.rules

sealed class Tag {
    data class Name(val text: String) : Tag()

    fun changeText(newText: String): Tag =
        when (this) {
            is Name -> Name(text = newText)
        }
}
