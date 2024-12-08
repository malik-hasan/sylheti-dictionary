package ui.screens.search.search

import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry

data class SearchOutputs(
    val detectedSearchScript: SearchScript,
    val results: List<DictionaryEntry>?,
    val recentSearches: List<String>
)
