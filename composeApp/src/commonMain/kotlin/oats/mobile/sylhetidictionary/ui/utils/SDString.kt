package oats.mobile.sylhetidictionary.ui.utils

import oats.mobile.sylhetidictionary.models.search.settings.SearchScript

data class SDString(
    val text: String,
    val highlightRegex: Regex = Regex(""),
    val script: SearchScript? = null
)
