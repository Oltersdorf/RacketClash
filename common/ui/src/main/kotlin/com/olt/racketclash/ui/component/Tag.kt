package com.olt.racketclash.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Tag(
    name: String,
    text: String? = null
) {
    if (text != null) {
        Row {
            Text(text = "$name: ", fontWeight = FontWeight.Bold)
            Text(text = "\"$text\"")
        }
    }
    else
        Text(text = name, fontWeight = FontWeight.Bold)
}