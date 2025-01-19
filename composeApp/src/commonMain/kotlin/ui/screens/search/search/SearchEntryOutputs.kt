package ui.screens.search.search

import models.CardEntry
import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry
import ui.utils.SDString

data class SearchEntryOutputs(
    val detectedSearchScript: SearchScript,
    val searchResults: List<DictionaryEntry>?,
    val entries: List<CardEntry>,
    val suggestions:  List<SDString>?,
    val recentSearches: List<String>
)
