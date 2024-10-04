package ui.screens.search

import oats.mobile.sylhetidictionary.DictionaryEntry

data class SearchState(
    val searchBarActive: Boolean = false,
    val searchTerm: String = "",
    val searchResults: List<DictionaryEntry>? = null,
    val bookmarks: List<DictionaryEntry> = emptyList()
)
