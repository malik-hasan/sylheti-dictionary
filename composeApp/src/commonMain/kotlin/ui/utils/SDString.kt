package ui.utils

import androidx.compose.ui.text.font.FontFamily

data class SDString(
    val text: String,
    val highlightRegex: Regex = Regex(""),
    val fontFamily: FontFamily? = null
)
