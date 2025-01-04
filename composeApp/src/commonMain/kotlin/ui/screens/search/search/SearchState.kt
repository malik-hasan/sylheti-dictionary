package ui.screens.search.search

import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry
import ui.utils.SDString

data class SearchState(
    val searchBarActive: Boolean = false,
    val searchResults: List<DictionaryEntry>? = null,
    val suggestions: List<SDString>? = null,
    val entryToBookmark: Map<DictionaryEntry, Boolean> = emptyMap(),
    val recents: List<String> = emptyList(),
    val detectedSearchScript: SearchScript = SearchScript.AUTO
)
