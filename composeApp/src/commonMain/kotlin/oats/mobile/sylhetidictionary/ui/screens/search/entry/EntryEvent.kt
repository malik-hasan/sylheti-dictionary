package oats.mobile.sylhetidictionary.ui.screens.search.entry

sealed interface EntryEvent {
    data class Bookmark(val entryId: String, val value: Boolean): EntryEvent
}
