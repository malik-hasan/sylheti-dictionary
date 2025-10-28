package oats.mobile.sylhetidictionary.ui.screens.search.search

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.ui.models.SDString

data class SearchState(
    val settingsMenuOpen: Boolean = false,
    val searchBarActive: Boolean = false,
    val resultsLoading: Boolean = false,
    val recents: List<SDString> = emptyList(),
    val suggestions: List<SDString> = emptyList(),
    val entries: List<DictionaryEntry> = emptyList(),
    val scrollCharIndexes: Map<Char, Int> = emptyMap(),
    val featureBengaliDefinitions: Boolean = false
)
