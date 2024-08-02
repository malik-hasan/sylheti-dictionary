package ui.screens.search

import oats.mobile.sylhetidictionary.DictionaryEntry

data class SearchState(
    val searchBarActive: Boolean = false,
    val searchResults: List<DictionaryEntry>? = null,
    val searchTerm: String = "",
    val bookmarks: List<DictionaryEntry> = emptyList(),
    val searchPositions: List<Boolean> = List(3) { false },
    val assetLoaded: Boolean = false
)
