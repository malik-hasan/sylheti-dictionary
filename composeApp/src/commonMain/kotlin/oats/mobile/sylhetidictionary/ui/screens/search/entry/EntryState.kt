package oats.mobile.sylhetidictionary.ui.screens.search.entry

import oats.mobile.sylhetidictionary.ComponentEntry
import oats.mobile.sylhetidictionary.DerivativeEntry
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.Example
import oats.mobile.sylhetidictionary.RelatedEntry
import oats.mobile.sylhetidictionary.Variant

data class EntryState(
    val entry: DictionaryEntry? = null,
    val isBookmark: Boolean = false,
    val referenceEntries: List<DictionaryEntry> = emptyList(),
    val variants: List<Variant> = emptyList(),
    val variantsExpanded: Boolean = false,
    val examples: List<Example> = emptyList(),
    val examplesExpanded: Boolean = false,
    val componentEntries: List<ComponentEntry> = emptyList(),
    val componentEntriesExpanded: Boolean = false,
    val derivativeEntries: List<DerivativeEntry> = emptyList(),
    val derivativeEntriesExpanded: Boolean = false,
    val relatedEntries: List<RelatedEntry> = emptyList(),
    val relatedEntriesExpanded: Boolean = false,
    val featureBengaliDefinitions: Boolean = false,
    val featureBengaliExamples: Boolean = false
)
