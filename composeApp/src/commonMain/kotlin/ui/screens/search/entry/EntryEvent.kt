package ui.screens.search.entry

sealed interface EntryEvent {
    data object ToggleBookmark: EntryEvent
}
