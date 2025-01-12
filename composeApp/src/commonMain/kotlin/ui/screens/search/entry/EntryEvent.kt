package ui.screens.search.entry

sealed interface EntryEvent {
    data class Bookmark(val entryId: String, val isBookmark: Boolean): EntryEvent
}
