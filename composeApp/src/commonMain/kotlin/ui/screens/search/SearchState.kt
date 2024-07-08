package ui.screens.search

import models.dictionary.DictionaryData

data class SearchState(
    val searchBarActive: Boolean = false,
    val searchResults: List<DictionaryData>? = null,
    val searchTerm: String = "",
    val favorites: List<DictionaryData> = emptyList(),
    val searchPositions: List<Boolean> = List(3) { false }
)
