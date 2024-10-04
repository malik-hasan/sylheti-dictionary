package ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.dictionary.DictionaryDataSource
import data.favorites.FavoritesRepository
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.search.settings.SearchPosition
import ui.utils.stateFlowOf

class SearchViewModel(
    private val dictionary: DictionaryDataSource,
    private val favorites: FavoritesRepository,
    private val preferences: PreferencesRepository
) : ViewModel() {

    // default to true so that error state doesn't show unless it actually fails
    private val _assetLoaded = MutableStateFlow(true)
    val assetLoaded: StateFlow<Boolean> = stateFlowOf(true,
        preferences.flow(PreferenceKey.CURRENT_DICTIONARY_VERSION, 0)
            .map { dictionaryVersion ->
                dictionaryVersion >= 0
            }
    )

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = stateFlowOf(SearchState(),
        combine(
            _searchState,
            favorites.getFavorites()
        ) { state, favorites ->
            state.copy(bookmarks = favorites,)
        }
    )

    private var searchJob: Job? = null

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            SearchEvent.Search -> setSearchBarActive(false)
            is SearchEvent.SetSearchBarActive -> setSearchBarActive(event.value)
            is SearchEvent.UpdateSearchTerm -> updateSearchTerm(event.value)
            SearchEvent.ClearSearchBar -> if (_searchState.value.searchTerm.isBlank()) {
                setSearchBarActive(false)
            } else updateSearchTerm("")

            is SearchEvent.SelectSuggestion -> updateSearchTerm(event.value) { error ->
                if (error == null) setSearchBarActive(false)
            }

            is SearchEvent.Bookmark -> with(event) {
                viewModelScope.launch {
                    if (isBookmark) {
                        favorites.addFavorite(entryId)
                    } else {
                        favorites.removeFavorite(entryId)
                    }
                }
            }
        }
    }

    private fun setSearchBarActive(value: Boolean) {
        _searchState.update { it.copy(searchBarActive = value) }
    }

    private fun updateSearchTerm(term: String, onCompletion: CompletionHandler = {}) {
        searchJob?.cancel()
        _searchState.update { it.copy(searchTerm = term) }
        if (_searchState.value.searchTerm.isBlank()) {
            _searchState.update { it.copy(searchResults = null) }
        } else {
            searchJob = viewModelScope.launch {
                Logger.d("SEARCH: Searching for $term")
                val results = dictionary.searchSylLatin("*$term*")
                Logger.d("SEARCH: Found ${results.size}")
                _searchState.update { it.copy(searchResults = results) }
            }
            searchJob?.invokeOnCompletion(onCompletion)
        }
    }

    private val _settingsState = MutableStateFlow(SearchSettingsState())
    val settingsState = stateFlowOf(SearchSettingsState(),
        combine(
            _settingsState,
            preferences.searchPositionFlow
        ) { state, positions ->
            state.copy(searchPositions = positions)
        }
    )

    fun onSettingsEvent(event: SearchSettingsEvent) {
        when (event) {
            is SearchSettingsEvent.ToggleSettingsMenu -> _settingsState.update {
                it.copy(menuExpanded = event.value)
            }

            is SearchSettingsEvent.SelectSearchPosition -> with(event) {
                viewModelScope.launch {
                    preferences.put(SearchPosition.entries[index].settingsKey, selected)
                }
            }
        }
    }
}
