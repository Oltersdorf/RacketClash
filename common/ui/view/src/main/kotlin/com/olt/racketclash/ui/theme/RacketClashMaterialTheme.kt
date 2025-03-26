package com.olt.racketclash.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*

@Composable
fun RacketClashMaterialTheme(content: @Composable (isDarkMode: Boolean, switchDarkMode: () -> Unit) -> Unit) {
    var isDarkMode by remember { mutableStateOf(false) }

    ProvideColors(additionalColorSchema = if (isDarkMode) AdditionalDarkColorSchema else AdditionalLightColorSchema) {
        MaterialTheme(
            colorScheme = if (isDarkMode) DarkColorSchema else LightColorSchema,
            content = { content(isDarkMode) { isDarkMode = !isDarkMode } }
        )
    }
}

@Composable
private fun ProvideColors(
    additionalColorSchema: AdditionalColorSchema,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        AdditionalMaterialTheme provides additionalColorSchema,
        content = content
    )
}