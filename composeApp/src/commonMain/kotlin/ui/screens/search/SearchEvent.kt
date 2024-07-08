package ui.screens.search

sealed interface SearchEvent {
    data object Search: SearchEvent
    data class SetSearchBarActive(val value: Boolean): SearchEvent
    data class UpdateSearchTerm(val value: String): SearchEvent
    data object ClearSearchBar: SearchEvent
    data class SelectSuggestion(val value: String): SearchEvent
    data class MarkFavorite(val entryId: String, val isFavorite: Boolean): SearchEvent
    data object LoadFavorites: SearchEvent
}
