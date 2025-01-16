package ui.screens.search.search

import models.CardEntry
import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry
import ui.utils.SDString

data class SearchState(
    val searchBarActive: Boolean = false,
    val searchResults: List<DictionaryEntry>? = null,
    val suggestions: List<SDString>? = null,
    val entries: List<CardEntry> = emptyList(),
    val recents: List<String> = emptyList(),
    val detectedSearchScript: SearchScript = SearchScript.AUTO
)
