package ui.screens.settings

import oats.mobile.sylhetidictionary.DictionaryEntry

data class SettingsState(
    val searchBarActive: Boolean = false,
    val searchResults: List<DictionaryEntry>? = null,
    val searchTerm: String = "",
    val bookmarks: List<DictionaryEntry> = emptyList(),
    val searchPositions: List<Boolean> = List(3) { false }
)
