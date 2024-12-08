package ui.screens.search.search

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.bookmarks.BookmarksDataSource
import data.dictionary.DictionaryDataSource
import data.recentsearches.RecentSearchesDataSource
import data.settings.PreferenceKey
import data.settings.PreferencesDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import models.search.settings.SearchLanguage
import models.search.settings.SearchPosition
import models.search.settings.SearchScript
import org.jetbrains.compose.resources.getString
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.at_least_one_language
import ui.screens.search.search.SearchEvent.Bookmark
import ui.screens.search.search.SearchEvent.Search
import ui.screens.search.search.SearchEvent.SelectSuggestion
import ui.screens.search.search.SearchEvent.SetSearchBarActive
import ui.screens.search.search.SearchEvent.UpdateSearchTerm
import ui.screens.search.search.SearchSettingsEvent.SelectPosition
import ui.screens.search.search.SearchSettingsEvent.SelectScript
import ui.screens.search.search.SearchSettingsEvent.ToggleSettingsMenu
import ui.utils.stateFlowOf
import utility.UnicodeUtility

class SearchViewModel(
    private val preferences: PreferencesDataSource,
    private val dictionaryDataSource: DictionaryDataSource,
    private val bookmarksDataSource: BookmarksDataSource,
    private val recentSearchesDataSource: RecentSearchesDataSource
) : ViewModel() {

    val assetLoaded = stateFlowOf(null,
        preferences.nullableFlow(PreferenceKey.CURRENT_DICTIONARY_VERSION).map { version ->
            version?.let { it >= 0 }
        }
    )

    val snackbarHostState = SnackbarHostState()

    private val _settingsState = MutableStateFlow(SearchSettingsState())
    val settingsState = stateFlowOf(
        SearchSettingsState(),
        with(preferences) {
            combine(
                _settingsState,
                searchPosition,
                searchScript,
                searchLanguages,
                combine(
                    flow(PreferenceKey.SEARCH_DEFINITIONS, false),
                    flow(PreferenceKey.SEARCH_EXAMPLES, false)
                ) { searchDefinitions, searchExamples -> searchDefinitions to searchExamples }
            ) { state, position, script, languages, (searchDefinitions, searchExamples) ->
                state.copy(
                    position = position,
                    script = script,
                    languages = languages.filterKeys { it in script.languages },
                    searchDefinitions = searchDefinitions,
                    searchExamples = searchExamples
                )
            }
        }
    )

    fun onSettingsEvent(event: SearchSettingsEvent) {
        when (event) {
            is ToggleSettingsMenu -> _settingsState.update {
                it.copy(menuExpanded = event.open)
            }

            is SelectPosition -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_POSITION, event.position.ordinal)
            }

            is SelectScript -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_SCRIPT, event.script.ordinal)
            }

            is SearchSettingsEvent.SelectLanguage -> with(event) {
                viewModelScope.launch {
                    val atLeastOneSelected = settingsState.value.languages.toMutableMap().apply {
                        this[language] = selected
                    }.any { it.value }

                    if (atLeastOneSelected) {
                        preferences.set(language.settingsKey, selected)
                    } else {
                        val message = getString(Res.string.at_least_one_language)
                        with(snackbarHostState) {
                            if (currentSnackbarData?.visuals?.message != message) {
                                currentSnackbarData?.dismiss()
                                showSnackbar(message)
                            }
                        }
                    }
                }
            }

            is SearchSettingsEvent.ToggleSearchDefinitions -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_DEFINITIONS, event.checked)
            }

            is SearchSettingsEvent.ToggleSearchExamples -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_EXAMPLES, event.checked)
            }
        }
    }

    var searchTerm by mutableStateOf("")
        private set

    private val searchOutputsFlow = combine(
        snapshotFlow { searchTerm },
        settingsState
    ) { searchTerm, settings ->
        coroutineScope {
            val detectedSearchScript = detectSearchScript(searchTerm, settings.script)
            val globSearchTerm = escapeGlobChars(searchTerm)
            val globMappedIpaTerm = mapIpaChars(globSearchTerm, detectedSearchScript)

            val results = async {
                getResults(
                    searchTerm = globSearchTerm,
                    mappedIpaTerm = globMappedIpaTerm,
                    detectedSearchScript = detectedSearchScript,
                    searchPosition = settings.position,
                    searchLanguages = settings.languages,
                    searchDefinitions = settings.searchDefinitions,
                    searchExamples = settings.searchExamples
                )
            }

            val recentSearches = async {
                recentSearchesDataSource.getRecentSearches(globSearchTerm, detectedSearchScript)
            }

            launch {
                preferences.set(PreferenceKey.HIGHLIGHT_REGEX, Regex.escape(searchTerm))
            }

            launch {
                val regexMappedIpaTerm = mapIpaChars(Regex.escape(searchTerm), detectedSearchScript, true)
                preferences.set(PreferenceKey.MAPPED_IPA_HIGHLIGHT_REGEX, regexMappedIpaTerm)
            }

            SearchOutputs(detectedSearchScript, results.await(), recentSearches.await())
        }
    }

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = stateFlowOf(SearchState(),
        combine(
            _searchState,
            bookmarksDataSource.bookmarksFlow,
            searchOutputsFlow
        ) { state, bookmarks, (detectedSearchScript, results, recentSearches) ->
            state.copy(
                searchResults = results,
                entryToBookmark = results?.associateWith { it.entryId in bookmarks }
                    ?: dictionaryDataSource.getEntries(bookmarks).associateWith { true },
                recents = recentSearches,
                detectedSearchScript = detectedSearchScript
            )
        }
    )

    private var previousSearchTerm = ""

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
                        bookmarksDataSource.addBookmark(entryId)
                    } else bookmarksDataSource.removeBookmark(entryId)
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
                            Logger.d("SEARCH: detected script: $script from term: $term")
                            return script
                        }
                    }
                }
            }
        }
        return searchScriptPreference
    }

    private fun escapeGlobChars(term: String) =
        term.map { char ->
            if (char in UnicodeUtility.GLOB_SPECIAL_CHARS) {
                "[$char]"
            } else char.toString()
        }.joinToString("")

    private fun mapIpaChars(term: String, detectedSearchScript: SearchScript, forRegex: Boolean = false) =
        if (detectedSearchScript.isLatinOrAuto) {
            term.map { char ->
                UnicodeUtility.LATIN_IPA_CHAR_MAP[char]?.let { altChars ->
                    val charSet = "[$char${altChars.joinToString("")}]"
                    if (forRegex) {
                        "\\E$charSet\\Q" // escape the escaping
                    } else charSet
                } ?: char.toString()
            }.joinToString("")
        } else term

    private fun getQueries(term: String, searchPosition: SearchPosition) =
        Pair("*$term*", searchPosition.getQuery(term))

    private suspend fun getResults(
        searchTerm: String,
        mappedIpaTerm: String,
        detectedSearchScript: SearchScript,
        searchPosition: SearchPosition,
        searchLanguages:  Map<SearchLanguage, Boolean>,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = searchTerm.takeIf { it.isNotBlank() }?.let {
        val (query, positionedQuery) = getQueries(searchTerm, searchPosition)
        Logger.d("SEARCH: getResults() $positionedQuery")

        yield()
        when (detectedSearchScript) {
            SearchScript.AUTO -> dictionaryDataSource.searchAll(query, positionedQuery, searchDefinitions, searchExamples)
            SearchScript.NAGRI -> dictionaryDataSource.searchNagri(query, positionedQuery, searchDefinitions, searchExamples)
            else -> detectedSearchScript.languages.filter { language ->
                settingsState.value.script == SearchScript.AUTO || searchLanguages[language] == true
            }.flatMap { language ->
                if (language == SearchLanguage.Latin.SYLHETI) {
                    val (mappedIpaQuery, mappedIpaPositionedQuery) = getQueries(mappedIpaTerm, searchPosition)
                    language.search(dictionaryDataSource, mappedIpaQuery, mappedIpaPositionedQuery, searchDefinitions, searchExamples)
                } else {
                    language.search(dictionaryDataSource, query, positionedQuery, searchDefinitions, searchExamples)
                }
            }
        }.sortedBy(detectedSearchScript.sortAlgorithm)
    }

    private fun setSearchBarActive(value: Boolean) {
        _searchState.update { it.copy(searchBarActive = value) }
    }

    private fun search() {
        setSearchBarActive(false)
        with(searchState.value) {
            if (searchResults?.isNotEmpty() == true) {
                viewModelScope.launch {
                    recentSearchesDataSource.cacheSearch(searchTerm, detectedSearchScript)
                }
            }
        }
    }
}
