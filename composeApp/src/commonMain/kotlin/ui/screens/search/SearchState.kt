package ui.screens.search

import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry

data class SearchState(
    val searchBarActive: Boolean = false,
    val searchBarPositions: String? = null,
    val searchBarScript: String? = null,
    val searchBarLanguage: String? = null,
    val searchResults: List<DictionaryEntry>? = null,
    val bookmarks: List<DictionaryEntry> = emptyList(),
    val recents: List<String> = emptyList(),
    val detectedSearchScript: SearchScript = SearchScript.AUTO,
    val highlightRegex: Regex = Regex("")
)
