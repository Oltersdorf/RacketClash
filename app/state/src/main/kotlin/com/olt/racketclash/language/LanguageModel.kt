package com.olt.racketclash.language

import com.olt.racketclash.state.ViewModelState

class LanguageModel : ViewModelState<LanguageModel.State>(State()) {

    data class State(
        val availableLanguages: Set<String> = setOf("English", "Deutsch"),
        val language: Language = English
    )

    private fun getLanguageFromString(language: String) =
        when (language) {
            English.languageName -> English
            German.languageName -> German
            else -> English
        }

    fun setLanguage(language: String) =
        onDefault {
            updateState { copy(language = getLanguageFromString(language = language)) }
        }
}