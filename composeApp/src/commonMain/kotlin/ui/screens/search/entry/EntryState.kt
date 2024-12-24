package ui.screens.search.entry

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.Domain
import oats.mobile.sylhetidictionary.Example
import oats.mobile.sylhetidictionary.Variant

data class EntryState(
    val entry: DictionaryEntry? = null,
    val isBookmark: Boolean = false,
    val examples: List<Example> = emptyList(),
    val variants: List<Variant> = emptyList(),
    val componentLexemes: List<DictionaryEntry> = emptyList(),
    val domains: List<Domain> = emptyList(),
    val relatedEntries: List<DictionaryEntry> = emptyList()
)
