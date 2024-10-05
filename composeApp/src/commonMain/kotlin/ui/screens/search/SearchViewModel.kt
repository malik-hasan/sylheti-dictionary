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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.search.settings.SearchScript
import ui.screens.search.SearchEvent.Bookmark
import ui.screens.search.SearchEvent.ClearSearchBar
import ui.screens.search.SearchEvent.Search
import ui.screens.search.SearchEvent.SelectSuggestion
import ui.screens.search.SearchEvent.SetSearchBarActive
import ui.screens.search.SearchEvent.UpdateSearchTerm
import ui.screens.search.SearchSettingsEvent.SelectPosition
import ui.screens.search.SearchSettingsEvent.SelectScript
import ui.screens.search.SearchSettingsEvent.ToggleSettingsMenu
import ui.utils.stateFlowOf

class SearchViewModel(
    private val dictionary: DictionaryDataSource,
    private val favorites: FavoritesRepository,
    private val preferences: PreferencesRepository
) : ViewModel() {

    // default to true so that error state doesn't show while loading, only after it actually fails
    val assetLoaded = stateFlowOf(true,
        preferences.flow(PreferenceKey.CURRENT_DICTIONARY_VERSION, 0).map { it >= 0 }
    )

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = stateFlowOf(SearchState(),
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
            is SetSearchBarActive -> setSearchBarActive(event.value)
            is UpdateSearchTerm -> updateSearchTerm(event.value)
            Search -> setSearchBarActive(false)
            ClearSearchBar -> if (_searchState.value.searchTerm.isBlank()) {
                setSearchBarActive(false)
            } else updateSearchTerm("")

            is SelectSuggestion -> updateSearchTerm(event.value) { error ->
                if (error == null) setSearchBarActive(false)
            }

            is Bookmark -> with(event) {
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
            preferences.searchPositionsFlow,
            preferences.flow(PreferenceKey.SEARCH_SCRIPT, 0),
            preferences.searchLanguagesFlow
        ) { state, positions, scriptOrdinal, languages ->
            val script = SearchScript.entries[scriptOrdinal]
            state.copy(
                positions = positions,
                script = script,
                languages = languages.filterKeys { it in script.languages }
            )
        }
    )

    fun onSettingsEvent(event: SearchSettingsEvent) {
        when (event) {
            is ToggleSettingsMenu -> _settingsState.update {
                it.copy(menuExpanded = event.value)
            }

            is SelectPosition -> with(event) {
                viewModelScope.launch {
                    preferences.set(position.settingsKey, selected)
                }
            }

            is SelectScript -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_SCRIPT, event.script.ordinal)
            }

            is SearchSettingsEvent.SelectLanguage -> with(event) {
                viewModelScope.launch {
                    preferences.set(language.settingsKey, selected)
                }
            }
        }
    }
}
