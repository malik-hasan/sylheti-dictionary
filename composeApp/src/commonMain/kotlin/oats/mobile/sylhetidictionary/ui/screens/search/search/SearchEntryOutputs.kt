package oats.mobile.sylhetidictionary.ui.screens.search.search

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.models.CardEntry
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.ui.utils.SDString

data class SearchEntryOutputs(
    val detectedSearchScript: SearchScript,
    val searchResults: List<DictionaryEntry>?,
    val entries: List<CardEntry>,
    val suggestions:  List<SDString>?,
    val recentSearches: List<String>
)
