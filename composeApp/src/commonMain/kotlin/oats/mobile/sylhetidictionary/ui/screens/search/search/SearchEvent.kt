package oats.mobile.sylhetidictionary.ui.screens.search.search

sealed interface SearchEvent {
    data object Search: SearchEvent
    data class SelectSuggestion(val term: String): SearchEvent
    data class ClearSearch(val searchBarExpanded: Boolean): SearchEvent
    data class Bookmark(val entryId: String, val bookmark: Boolean): SearchEvent
}
