package ui.screens.search.entry

import oats.mobile.sylhetidictionary.ComponentEntry
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.Example
import oats.mobile.sylhetidictionary.RelatedEntry
import oats.mobile.sylhetidictionary.Variant

data class EntryState(
    val entry: DictionaryEntry? = null,
    val isBookmark: Boolean = false,
    val examples: List<Example> = emptyList(),
    val variants: List<Variant> = emptyList(),
    val componentLexemeToBookmark: Map<ComponentEntry, Boolean> = emptyMap(),
    val relatedEntryToBookmark: Map<RelatedEntry, Boolean> = emptyMap()
)
