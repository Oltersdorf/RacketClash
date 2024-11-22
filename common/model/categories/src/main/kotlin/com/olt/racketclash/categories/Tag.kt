package com.olt.racketclash.categories

sealed class Tag {
    data class Name(val text: String) : Tag()
    data object Finished : Tag()
    data object NotFinished : Tag()

    fun changeText(newText: String): Tag =
        when (this) {
            is Name -> Name(text = newText)
            Finished -> this
            NotFinished -> this
        }
}