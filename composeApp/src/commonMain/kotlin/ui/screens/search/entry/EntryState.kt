package ui.screens.search.entry

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.Example

data class EntryState(
    val entry: DictionaryEntry? = null,
    val isBookmark: Boolean = false,
    val examples: List<Example> = listOf()
)
