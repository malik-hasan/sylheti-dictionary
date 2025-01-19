package ui.screens.search.search

import models.CardEntry
import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry
import ui.utils.SDString

data class SearchOutputs(
    val detectedSearchScript: SearchScript,
    val searchResults: List<DictionaryEntry>?,
    val suggestions:  List<SDString>?,
    val recentSearches: List<String>,
    val regexSearchTerm: String,
    val regexMappedIpaTerm: String
) {
    fun toSearchEntryOutputs(entries: List<CardEntry>) = SearchEntryOutputs(
        detectedSearchScript = detectedSearchScript,
        searchResults = searchResults,
        entries = entries,
        suggestions = suggestions,
        recentSearches = recentSearches
    )
}
