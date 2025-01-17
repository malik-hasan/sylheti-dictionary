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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import models.CardEntry
import models.displayBengali
import models.displayIPA
import models.displayNagri
import models.search.settings.SearchLanguage
import models.search.settings.SearchPosition
import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.VariantEntry
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
import ui.utils.SDString
import ui.utils.stateFlowOf
import utility.UnicodeUtility

@OptIn(ExperimentalCoroutinesApi::class)
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
        searchTerm to settings
    }.transformLatest { (searchTerm, settings) ->
        coroutineScope {
            val detectSearchScriptJob = async { detectSearchScript(searchTerm, settings.script) }

            val globSearchTerm = mapChars(
                term = mapChars(escapeGlobChars(searchTerm), UnicodeUtility.STOP_CHAR_MAP),
                charMap = UnicodeUtility.CASE_MAP
            )
            val globMappedIpaTerm = mapChars(globSearchTerm, UnicodeUtility.LATIN_IPA_CHAR_MAP, remapped = true)
            val regexSearchTerm = mapChars(Regex.escape(searchTerm), UnicodeUtility.STOP_CHAR_MAP, forRegex = true)
            val regexMappedIpaTerm = mapChars(regexSearchTerm, UnicodeUtility.LATIN_IPA_CHAR_MAP, forRegex = true)
            val highlightRegex = Regex(regexSearchTerm, RegexOption.IGNORE_CASE)
            val mappedIpaHighlightRegex = Regex(regexMappedIpaTerm, RegexOption.IGNORE_CASE)

            Logger.d("SEARCH: GLOB: $globSearchTerm")
            Logger.d("SEARCH: GLOB IPA: $globMappedIpaTerm")
            Logger.d("SEARCH: REGEX: $regexSearchTerm")
            Logger.d("SEARCH: REGEX IPA: $regexMappedIpaTerm")

            val detectedSearchScript = detectSearchScriptJob.await()

            val searchResultsJob = async {
                getSearchResults(
                    searchTerm = globSearchTerm,
                    mappedIpaTerm = globMappedIpaTerm,
                    detectedSearchScript = detectedSearchScript,
                    searchPosition = settings.position,
                    searchLanguages = settings.languages,
                    searchDefinitions = settings.searchDefinitions,
                    searchExamples = settings.searchExamples
                )
            }

            val recentSearchesJob = async {
                recentSearchesDataSource.getRecentSearches(settings.position.getQuery(globMappedIpaTerm), detectedSearchScript)
            }

            launch {
                preferences.set(PreferenceKey.HIGHLIGHT_REGEX, regexSearchTerm)
            }

            launch {
                preferences.set(PreferenceKey.MAPPED_IPA_HIGHLIGHT_REGEX, regexMappedIpaTerm)
            }

            val searchResults = searchResultsJob.await()
            val recentSearches = recentSearchesJob.await()

            val suggestions = getSuggestions(
                searchResults = searchResults,
                recentSearches = recentSearches,
                detectedSearchScript = detectedSearchScript,
                highlightRegex = highlightRegex,
                mappedIpaHighlightRegex = mappedIpaHighlightRegex,
                searchLanguages = settings.languages
            )

            emit(SearchOutputs(detectedSearchScript, searchResults, suggestions, recentSearches))
        }
    }

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = stateFlowOf(SearchState(),
        combine(
            _searchState,
            bookmarksDataSource.bookmarksFlow,
            searchOutputsFlow
        ) { state, bookmarks, searchOutputs ->
            Triple(state, bookmarks, searchOutputs)
        }.transformLatest { (state, bookmarks, searchOutputs) ->
            val (detectedSearchScript, searchResults, suggestions, recentSearches) = searchOutputs
            emit(state.copy(
                searchResults = searchResults,
                suggestions = suggestions,
                entries = (searchResults ?: dictionaryDataSource.getEntries(bookmarks)).mapNotNull {
                    var variantEntries = emptyList<VariantEntry>()
                    if (it.definitionEN.isNullOrBlank()) {
                        variantEntries = dictionaryDataSource.getVariantEntries(it.entryId)
                        if (variantEntries.isEmpty()) return@mapNotNull null
                    }

                    CardEntry(
                        dictionaryEntry = it,
                        isBookmark = it.entryId in bookmarks,
                        variantEntries = variantEntries
                    )
                },
                recents = recentSearches,
                detectedSearchScript = detectedSearchScript
            ))
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
                    with(bookmarksDataSource) {
                        if (isBookmark) {
                            addBookmark(entryId)
                        } else removeBookmark(entryId)
                    }
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

    private fun mapChars(term: String, charMap: Map<Char, Set<Char>>, forRegex: Boolean = false, remapped: Boolean = false) =
        term.map { char ->
            charMap[char]?.let { altChars ->
                var charSet = "$char${altChars.joinToString("")}"
                if (!remapped) charSet = "[$charSet]"
                if (forRegex) {
                    "\\E$charSet\\Q" // escape the escaping
                } else charSet
            } ?: char.toString()
        }.joinToString("")

    private fun getQueries(term: String, searchPosition: SearchPosition) =
        Pair("*$term*", searchPosition.getQuery(term))

    private suspend fun getSearchResults(
        searchTerm: String,
        mappedIpaTerm: String,
        detectedSearchScript: SearchScript,
        searchPosition: SearchPosition,
        searchLanguages:  Map<SearchLanguage, Boolean>,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = searchTerm.takeIf { it.isNotBlank() }?.let {
        val (query, positionedQuery) = getQueries(searchTerm, searchPosition)
        Logger.d("SEARCH: getSearchResults() $positionedQuery")

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

    private suspend fun getSuggestions(
        searchResults: List<DictionaryEntry>?,
        recentSearches: List<String>,
        detectedSearchScript: SearchScript,
        highlightRegex: Regex,
        mappedIpaHighlightRegex: Regex,
        searchLanguages: Map<SearchLanguage, Boolean>
    ) = searchResults?.let { results ->
        val suggestions = mutableSetOf<SDString>()
        results.forEach { entry ->
            yield()
            with(entry) {
                when {
                    detectedSearchScript == SearchScript.BENGALI && displayBengali != null ->
                        suggestions += SDString(displayBengali!!, highlightRegex, SearchScript.BENGALI)

                    detectedSearchScript == SearchScript.NAGRI && displayNagri != null ->
                        suggestions += SDString(displayNagri!!, highlightRegex)

                    else -> {
                        if (displayIPA.contains(mappedIpaHighlightRegex) && (
                            searchLanguages.isEmpty() || searchLanguages[models.search.settings.SearchLanguage.Latin.SYLHETI] == true
                        )) {
                            suggestions += SDString(displayIPA, mappedIpaHighlightRegex, SearchScript.LATIN)
                        }

                        if (gloss?.contains(highlightRegex) == true && (
                            searchLanguages.isEmpty() || searchLanguages[models.search.settings.SearchLanguage.Latin.ENGLISH] == true
                        )) {
                            suggestions += SDString(gloss, highlightRegex, SearchScript.LATIN)
                        }
                    }
                }
            }
        }
        suggestions.filter { it.text !in recentSearches }
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
