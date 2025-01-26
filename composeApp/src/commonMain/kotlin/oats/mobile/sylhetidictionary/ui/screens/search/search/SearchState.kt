package oats.mobile.sylhetidictionary.ui.screens.search.search

import oats.mobile.sylhetidictionary.models.CardEntry
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.ui.utils.SDString

data class SearchState(
    val menuExpanded: Boolean = false,
    val searchBarActive: Boolean = false,
    val searchResults: List<DictionaryEntry>? = null,
    val suggestions: List<SDString>? = null,
    val entries: List<CardEntry> = emptyList(),
    val recents: List<String> = emptyList(),
    val detectedSearchScript: SearchScript = SearchScript.AUTO
)
