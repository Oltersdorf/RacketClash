package com.olt.racketclash.language

import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.language.translations.English
import com.olt.racketclash.language.translations.German
import com.olt.racketclash.language.translations.Language

class LanguageHandler(private val fileHandler: FileHandler) {

    val availableLanguages: Set<String> = setOf("English", "Deutsch")

    fun setLanguage(language: String): Language {
        val selectedLanguage = when (language) {
            "English" -> English
            "Deutsch" -> German
            else -> English
        }

        fileHandler.setLanguage(language = selectedLanguage.languageName)

        return selectedLanguage
    }
}