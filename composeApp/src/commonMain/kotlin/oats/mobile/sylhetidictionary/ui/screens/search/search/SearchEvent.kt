package oats.mobile.sylhetidictionary.ui.screens.search.search

sealed interface SearchEvent {
    data class OpenSettingsMenu(val open: Boolean): SearchEvent
    data class SetSearchBarActive(val active: Boolean): SearchEvent
    data class UpdateSearchTerm(val term: String): SearchEvent
    data object Search: SearchEvent
    data class SelectSuggestion(val term: String): SearchEvent
    data class ToggleBookmark(val entryId: String, val bookmark: Boolean): SearchEvent
}
