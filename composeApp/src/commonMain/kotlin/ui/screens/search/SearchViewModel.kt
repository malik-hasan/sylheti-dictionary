package ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.dictionary.DictionaryDataSource
import data.bookmarks.BookmarksRepository
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
    private val bookmarks: BookmarksRepository,
    private val preferences: PreferencesRepository
) : ViewModel() {

    // default to true so that error state doesn't show while loading, only after it actually fails
    val assetLoaded = stateFlowOf(true,
        preferences.flow(PreferenceKey.CURRENT_DICTIONARY_VERSION, 0).map { it >= 0 }
    )

    var searchTerm by mutableStateOf("")
        private set

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = stateFlowOf(SearchState(),
        combine(
            _searchState,
            bookmarks.getBookmarks()
        ) { state, bookmarks ->
            state.copy(bookmarks = bookmarks)
        }
    )

    private var searchJob: Job? = null

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SetSearchBarActive -> setSearchBarActive(event.value)
            is UpdateSearchTerm -> updateSearchTerm(event.value)
            Search -> setSearchBarActive(false)
            ClearSearchBar -> if (searchTerm.isBlank()) {
                setSearchBarActive(false)
            } else updateSearchTerm("")

            is SelectSuggestion -> updateSearchTerm(event.value) { error ->
                if (error == null) setSearchBarActive(false)
            }

            is Bookmark -> with(event) {
                viewModelScope.launch {
                    if (isBookmark) {
                        bookmarks.addBookmark(entryId)
                    } else {
                        bookmarks.removeBookmark(entryId)
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
        searchTerm = term
        if (searchTerm.isBlank()) {
            _searchState.update { it.copy(searchResults = null) }
        } else {
            searchJob = viewModelScope.launch {
                Logger.d("SEARCH: Searching for $term")
                val results = dictionary.searchSylLatin("*$term*")
                Logger.d("SEARCH: Found ${results.size}")
                Logger.d("SEARCH: Found ${results.take(5).map { it.lexemeIPA }}")
                _searchState.update { it.copy(searchResults = results) }
            }
            searchJob?.invokeOnCompletion(onCompletion)
        }
    }

    private val _settingsState = MutableStateFlow(SearchSettingsState())
    val settingsState = stateFlowOf(SearchSettingsState(),
        combine(
            _settingsState,
            preferences.searchPositions,
            preferences.searchScript,
            preferences.searchLanguages
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
                    if (settingsState.value.positions.none { it }) { // ensure at least one is selected
                        preferences.set(position.settingsKey, true)
                    }
                }
            }

            is SelectScript -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_SCRIPT, event.script.ordinal)
            }

            is SearchSettingsEvent.SelectLanguage -> with(event) {
                viewModelScope.launch {
                    preferences.set(language.settingsKey, selected)
                    if (settingsState.value.languages.none { it.value }) { // ensure at least one is selected
                        preferences.set(language.settingsKey, true)
                    }
                }
            }
        }
    }
}
