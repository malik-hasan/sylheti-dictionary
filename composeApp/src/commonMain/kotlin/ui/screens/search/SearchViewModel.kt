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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.search.settings.SearchPosition

class SearchViewModel(
    private val dictionary: DictionaryDataSource,
    private val favorites: FavoritesRepository,
    private val preferences: PreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = combine(
        combine(
            _state,
            preferences.flow(PreferenceKey.CURRENT_DICTIONARY_VERSION, 0),
            preferences.flow(PreferenceKey.SEARCH_BEGINNING, false),
            preferences.flow(PreferenceKey.SEARCH_MIDDLE, false),
            preferences.flow(PreferenceKey.SEARCH_END, false)
        ) { state, dictionaryVersion, searchBeginning, searchMiddle, searchEnd ->
            state.copy(
                assetLoaded = dictionaryVersion >= 0,
                searchPositions = listOf(searchBeginning, searchMiddle, searchEnd)
            )
        },
        favorites.getFavorites(),
    ) { state, favorites ->
        state.copy(bookmarks = favorites)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SearchState())

    private var searchJob: Job? = null

    fun onEvent(event: SearchEvent) {
        when (event) {
            SearchEvent.Search -> setSearchBarActive(false)
            is SearchEvent.SetSearchBarActive -> setSearchBarActive(event.value)
            is SearchEvent.UpdateSearchTerm -> updateSearchTerm(event.value)
            SearchEvent.ClearSearchBar -> if (_state.value.searchTerm.isBlank()) {
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

            is SearchEvent.ToggleSettingsMenu -> _state.update {
                it.copy(settingsMenuOpen = event.value)
            }

            is SearchEvent.SelectSearchPosition -> with(event) {
                viewModelScope.launch {
                    preferences.put(SearchPosition.entries[index].settingsKey, selected)
                }
            }
        }
    }

    private fun setSearchBarActive(value: Boolean) {
        _state.update { it.copy(searchBarActive = value) }
    }

    private fun updateSearchTerm(term: String, onCompletion: CompletionHandler = {}) {
        searchJob?.cancel()
        _state.update { it.copy(searchTerm = term) }
        if (_state.value.searchTerm.isBlank()) {
            _state.update { it.copy(searchResults = null) }
        } else {
            searchJob = viewModelScope.launch {
                Logger.d("SEARCH: Searching for $term")
                val results = dictionary.searchSylLatin("*$term*")
                Logger.d("SEARCH: Found ${results.size}")
                _state.update { it.copy(searchResults = results) }
            }
            searchJob?.invokeOnCompletion(onCompletion)
        }
    }
}
