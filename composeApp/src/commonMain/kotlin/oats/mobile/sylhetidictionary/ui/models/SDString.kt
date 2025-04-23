package oats.mobile.sylhetidictionary.ui.models

import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchScript

data class SDString(
    val text: String,
    val highlightRegex: Regex = Regex(""),
    val script: SearchScript? = null
)
