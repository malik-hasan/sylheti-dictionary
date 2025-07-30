package oats.mobile.sylhetidictionary.ui.screens.search.entry

sealed interface EntryEvent {
    data class Bookmark(val entryId: String, val bookmark: Boolean): EntryEvent
    data object ToggleVariants: EntryEvent
    data object ToggleExamples: EntryEvent
    data object ToggleComponentLexemes: EntryEvent
    data object ToggleDerivativeLexemes: EntryEvent
    data object ToggleRelatedWords: EntryEvent
}
