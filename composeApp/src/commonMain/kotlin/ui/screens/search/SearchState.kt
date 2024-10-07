package ui.screens.search

import oats.mobile.sylhetidictionary.DictionaryEntry

data class SearchState(
    val searchBarActive: Boolean = false,
    val searchResults: List<DictionaryEntry>? = null,
    val bookmarks: List<DictionaryEntry> = emptyList()
)
