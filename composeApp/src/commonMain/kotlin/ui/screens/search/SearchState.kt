package ui.screens.search

import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry

data class SearchState(
    val searchBarActive: Boolean = false,
    val searchResults: List<DictionaryEntry>? = null,
    val bookmarks: List<DictionaryEntry> = emptyList(),
    val recents: List<String> = emptyList(),
    val detectedSearchScript: SearchScript = SearchScript.AUTO,
    val highlightRegex: Regex = Regex("")
)
