package oats.mobile.sylhetidictionary.ui.screens.search.entry

import oats.mobile.sylhetidictionary.ComponentEntry
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.Example
import oats.mobile.sylhetidictionary.RelatedEntry
import oats.mobile.sylhetidictionary.Variant
import oats.mobile.sylhetidictionary.VariantEntry
import oats.mobile.sylhetidictionary.models.CardEntry

data class EntryState(
    val entry: DictionaryEntry? = null,
    val isBookmark: Boolean = false,
    val examples: List<Example> = emptyList(),
    val variants: List<Variant> = emptyList(),
    val variantEntries: List<VariantEntry> = emptyList(),
    val componentLexemes: Map<ComponentEntry, CardEntry> = emptyMap(),
    val relatedEntries: Map<RelatedEntry, CardEntry> = emptyMap()
)
