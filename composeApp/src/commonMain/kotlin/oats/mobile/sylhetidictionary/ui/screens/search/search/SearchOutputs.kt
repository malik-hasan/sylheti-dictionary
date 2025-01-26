package oats.mobile.sylhetidictionary.ui.screens.search.search

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.models.CardEntry
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.ui.utils.SDString

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
