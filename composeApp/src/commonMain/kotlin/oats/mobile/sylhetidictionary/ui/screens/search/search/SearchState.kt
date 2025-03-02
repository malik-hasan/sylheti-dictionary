package oats.mobile.sylhetidictionary.ui.screens.search.search

import oats.mobile.sylhetidictionary.models.CardEntry
import oats.mobile.sylhetidictionary.ui.utils.SDString

data class SearchState(
    val menuExpanded: Boolean = false,
    val searchBarActive: Boolean = false,
    val resultsLoading: Boolean = false,
    val recents: List<SDString> = emptyList(),
    val suggestions: List<SDString> = emptyList(),
    val entries: List<CardEntry> = emptyList()
)
