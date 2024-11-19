package ui.screens.search

import oats.mobile.sylhetidictionary.Example

data class ExtendedEntryData(
    val isBookmark: Boolean = false,
    val isExpanded: Boolean = false,
    val examples: List<Example> = emptyList()
)
