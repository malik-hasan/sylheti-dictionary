package ui.screens.search

import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry

data class SearchOutputs(
    val detectedSearchScript: SearchScript,
    val regexMappedIpaTerm: String,
    val results: List<DictionaryEntry>?,
    val recentSearches: List<String>
)
