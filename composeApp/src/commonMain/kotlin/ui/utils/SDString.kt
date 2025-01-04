package ui.utils

import models.search.settings.SearchScript

data class SDString(
    val text: String,
    val highlightRegex: Regex = Regex(""),
    val script: SearchScript? = null
)
