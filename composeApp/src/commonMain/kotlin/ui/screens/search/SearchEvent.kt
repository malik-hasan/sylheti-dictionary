package ui.screens.search

import oats.mobile.sylhetidictionary.DictionaryEntry

sealed interface SearchEvent {
    data class SetSearchBarActive(val value: Boolean): SearchEvent
    data class UpdateSearchTerm(val value: String): SearchEvent
    data object Search: SearchEvent
    data class SelectSuggestion(val value: String): SearchEvent
    data class Bookmark(val entryId: String, val isBookmark: Boolean): SearchEvent
    data class ExpandItem(val entry: DictionaryEntry, val isExpanded: Boolean): SearchEvent
}
