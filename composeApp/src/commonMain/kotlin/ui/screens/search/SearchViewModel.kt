package ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.bookmarks.BookmarksRepository
import data.dictionary.DictionaryDataSource
import data.recentsearches.RecentSearchesRepository
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import models.search.settings.SearchScript
import ui.screens.search.SearchEvent.Bookmark
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
    private val preferences: PreferencesRepository,
    private val recentSearches: RecentSearchesRepository
) : ViewModel() {

    val assetLoaded = stateFlowOf(null,
        preferences.nullableFlow(PreferenceKey.CURRENT_DICTIONARY_VERSION).map { version ->
            version?.let { it >= 0 }
        }
    )

    private val _settingsState = MutableStateFlow(SearchSettingsState())
    val settingsState = stateFlowOf(SearchSettingsState(),
        combine(
            _settingsState,
            preferences.searchPositions,
            preferences.searchScript,
            preferences.searchLanguages
        ) { state, positions, script, languages ->
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
                    val atLeastOneSelected = settingsState.value.positions.toMutableList().apply {
                        this[position.ordinal] = selected
                    }.any { it }

                    if (atLeastOneSelected) preferences.set(position.settingsKey, selected)
                }
            }

            is SelectScript -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_SCRIPT, event.script.ordinal)
            }

            is SearchSettingsEvent.SelectLanguage -> with(event) {
                viewModelScope.launch {
                    val atLeastOneSelected = settingsState.value.languages.toMutableMap().apply {
                        this[language] = selected
                    }.any { it.value }

                    if (atLeastOneSelected) preferences.set(language.settingsKey, selected)
                }
            }
        }
    }

    var searchTerm by mutableStateOf("")
        private set

    private var previousSearchTerm = ""

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = stateFlowOf(SearchState(),
        combine(
            _searchState,
            bookmarks.getBookmarks(),
            snapshotFlow { searchTerm },
            settingsState
        ) { state, bookmarks, searchTerm, settings ->
            val detectedSearchScript = detectSearchScript(searchTerm, settings.script)
            state.copy(
                searchResults = getResults(searchTerm, detectedSearchScript),
                bookmarks = bookmarks,
                recents = recentSearches.getRecentSearches(searchTerm, detectedSearchScript),
                detectedSearchScript = detectedSearchScript
            )
        }
    )

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SetSearchBarActive -> with(event) {
                if (value) {
                    previousSearchTerm = searchTerm
                } else searchTerm = previousSearchTerm
                setSearchBarActive(value)
            }

            is UpdateSearchTerm -> searchTerm = event.value
            Search -> search()
            is SelectSuggestion -> {
                searchTerm = event.value
                search()
            }

            is Bookmark -> with(event) {
                viewModelScope.launch {
                    if (isBookmark) {
                        bookmarks.addBookmark(entryId)
                    } else bookmarks.removeBookmark(entryId)
                }
            }
        }
    }

    private suspend fun detectSearchScript(term: String, searchScriptPreference: SearchScript): SearchScript {

        if (searchScriptPreference == SearchScript.AUTO) {
            term.forEach { char ->
                yield()
                SearchScript.entries.forEach { script ->
                    yield()
                    script.regexCharSet?.let { regex ->
                        if (char.toString().matches(regex)) {
                            Logger.d("SEARCH: detected script: $script from term: $term, pref: $searchScriptPreference")
                            return script
                        }
                    }
                }
            }
        }
        return searchScriptPreference
    }

    private suspend fun getResults(term: String, detectedSearchScript: SearchScript) =
        term.takeIf { it.isNotBlank() }?.let {
            val query = "*$term*"

            if (detectedSearchScript == SearchScript.NAGRI) {
                dictionary.searchNagri(query)
            } else {
                detectedSearchScript.languages.filter { language ->
                    settingsState.value.script == SearchScript.AUTO || preferences.get(language.settingsKey) == true
                }.flatMap { language ->
                    language.search(dictionary, query)
                }
            }
        }

    private fun setSearchBarActive(value: Boolean) {
        _searchState.update { it.copy(searchBarActive = value) }
    }

    private fun search() {
        setSearchBarActive(false)
        with(searchState.value) {
            if (searchResults?.isNotEmpty() == true) {
                viewModelScope.launch {
                    recentSearches.cacheSearch(searchTerm, detectedSearchScript)
                }
            }
        }
    }
}
