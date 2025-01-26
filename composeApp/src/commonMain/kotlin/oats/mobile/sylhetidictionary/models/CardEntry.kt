package oats.mobile.sylhetidictionary.models

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.VariantEntry

data class CardEntry(
    val dictionaryEntry: DictionaryEntry,
    val isBookmark: Boolean,
    val variantEntries: List<VariantEntry>
)
