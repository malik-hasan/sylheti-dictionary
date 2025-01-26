package oats.mobile.sylhetidictionary.ui.screens.search.search

sealed interface SearchEvent {
    data class ToggleSettingsMenu(val expanded: Boolean): SearchEvent
    data class SetSearchBarActive(val value: Boolean): SearchEvent
    data class UpdateSearchTerm(val value: String): SearchEvent
    data object Search: SearchEvent
    data class SelectSuggestion(val value: String): SearchEvent
    data class Bookmark(val entryId: String, val isBookmark: Boolean): SearchEvent
}
