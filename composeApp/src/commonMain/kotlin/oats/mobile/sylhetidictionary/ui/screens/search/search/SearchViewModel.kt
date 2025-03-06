package oats.mobile.sylhetidictionary.ui.screens.search.search

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.VariantEntry
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryRepository
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesRepository
import oats.mobile.sylhetidictionary.data.settings.PreferenceKey
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.models.CardEntry
import oats.mobile.sylhetidictionary.models.displayBengali
import oats.mobile.sylhetidictionary.models.displayIPA
import oats.mobile.sylhetidictionary.models.displayNagri
import oats.mobile.sylhetidictionary.models.search.settings.SearchLanguage
import oats.mobile.sylhetidictionary.models.search.settings.SearchPosition
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.ui.utils.SDString
import oats.mobile.sylhetidictionary.ui.utils.stateFlowOf
import oats.mobile.sylhetidictionary.utility.UnicodeUtility
import org.jetbrains.compose.resources.getString
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.at_least_one_language

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val preferences: PreferencesRepository,
    private val dictionaryRepository: DictionaryRepository,
    private val bookmarksRepository: BookmarksRepository,
    private val recentSearchesRepository: RecentSearchesRepository
) : ViewModel() {

    val assetLoaded = stateFlowOf(null,
        preferences.nullableFlow(PreferenceKey.CURRENT_DICTIONARY_VERSION).map { version ->
            version?.let { it >= 0 }
        }
    )

    val snackbarHostState = SnackbarHostState()

    val settingsState = stateFlowOf(SearchSettingsState(),
        with(preferences) {
            combine(
                searchPosition,
                searchScript,
                searchLanguages,
                flow(PreferenceKey.SEARCH_DEFINITIONS, false),
                flow(PreferenceKey.SEARCH_EXAMPLES, false)
            ) { position, script, languages, searchDefinitions, searchExamples ->
                SearchSettingsState(
                    position = position,
                    script = script,
                    languages = languages.filterKeys { it in script.languages },
                    searchDefinitions = searchDefinitions,
                    searchExamples = searchExamples
                ).also {
                    Logger.d("SEARCH: refreshing search for settings change")
                    refreshSearch(searchTerm, it)
                }
            }
        }
    )

    fun onSettingsEvent(event: SearchSettingsEvent) {
        when (event) {
            is SearchSettingsEvent.SelectPosition -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_POSITION, event.position.ordinal)
            }

            is SearchSettingsEvent.SelectScript -> viewModelScope.launch {
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

    private val searchSuggestionsFlow = settingsState.combine(
        snapshotFlow { searchTerm }.debounce(300)
    ) { settings, searchTerm ->
        getSearchSuggestions(searchTerm, settings)
    }

    private val searchResultsStateFlow = MutableStateFlow<List<DictionaryEntry>?>(null)

    private val cardEntriesFlow = searchResultsStateFlow.combine(
        bookmarksRepository.bookmarksFlow
    ) { searchResults, bookmarks ->
        coroutineScope {
            val entries = (searchResults ?: dictionaryRepository.getEntries(bookmarks)).mapNotNull {
                var variantEntries = emptyList<VariantEntry>()
                if (it.definitionEN.isNullOrBlank()) {
                    variantEntries = dictionaryRepository.getVariantEntries(it.entryId)
                    if (variantEntries.isEmpty()) return@mapNotNull null
                }

                CardEntry(
                    dictionaryEntry = it,
                    isBookmark = it.entryId in bookmarks,
                    variantEntries = variantEntries
                )
            }

            _searchState.update { it.copy(resultsLoading = false) }
            entries
        }
    }

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = stateFlowOf(SearchState(),
        combine(
            _searchState,
            searchSuggestionsFlow,
            cardEntriesFlow
        ) { state, (recents, suggestions), entries ->
            state.copy(
                recents = recents,
                suggestions = suggestions,
                entries = entries
            )
        }
    )

    private var previousSearchTerm = ""

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.ToggleSettingsMenu -> _searchState.update {
                it.copy(menuExpanded = event.expanded)
            }

            is SearchEvent.SetSearchBarActive -> with(event) {
                if (value) {
                    previousSearchTerm = searchTerm
                } else searchTerm = previousSearchTerm
                setSearchBarActive(value)
            }

            is SearchEvent.UpdateSearchTerm -> searchTerm = event.value
            SearchEvent.Search -> search()
            is SearchEvent.SelectSuggestion -> {
                searchTerm = event.value
                search()
            }

            is SearchEvent.Bookmark -> with(event) {
                viewModelScope.launch {
                    with(bookmarksRepository) {
                        if (isBookmark) {
                            addBookmark(entryId)
                        } else removeBookmark(entryId)
                    }
                }
            }
        }
    }

    private fun setSearchBarActive(value: Boolean) {
        _searchState.update { it.copy(searchBarActive = value) }
    }

    private fun search() {
        setSearchBarActive(false)
        Logger.d("SEARCH: searching...")

        viewModelScope.launch {
            val searchTerm = searchTerm

            val (hasSearchResults, detectedSearchScript, highlightRegex) = refreshSearch(searchTerm, settingsState.value)

            preferences.setHighlightRegex(highlightRegex)

            if (hasSearchResults) {
                recentSearchesRepository.cacheSearch(searchTerm, detectedSearchScript)
            }
        }
    }

    private suspend fun processSearchQuery(searchTerm: String, settings: SearchSettingsState) = coroutineScope {
        val globSearchTermJob = async {
            searchTerm.takeIf { it.isNotBlank() }?.let {
                mapChars(
                    term = mapChars(
                        term = escapeGlobChars(it),
                        charMap = UnicodeUtility.STOP_CHAR_MAP + UnicodeUtility.CASE_MAP
                    ),
                    charMap = UnicodeUtility.LATIN_IPA_CHAR_MAP,
                    remapped = true
                )
            }.also { Logger.d("SEARCH: globSearchTerm: $it") }
        }

        val detectSearchScriptJob = async { detectSearchScript(searchTerm, settings.script) }

        val highlightRegexJob = async {
            val regexSearchTerm = if (searchTerm.isBlank()) {
                ""
            } else mapChars(
                term = Regex.escape(searchTerm.lowercase()),
                charMap = UnicodeUtility.STOP_CHAR_MAP + UnicodeUtility.LATIN_IPA_CHAR_MAP,
                forRegex = true
            )

            Logger.d("SEARCH: highlight regex: $regexSearchTerm")
            Regex(regexSearchTerm, RegexOption.IGNORE_CASE)
        }

        Triple(
            globSearchTermJob.await(),
            detectSearchScriptJob.await(),
            highlightRegexJob.await()
        )
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
                if (forRegex) charSet = "\\E$charSet\\Q" // escape the escaping
                charSet
            } ?: char.toString()
        }.joinToString("")

    private suspend fun getQueryResults(
        positionedQuery: String,
        detectedSearchScript: SearchScript,
        settings: SearchSettingsState,
        simpleQuery: String = "",
        searchDefinitions: Boolean = false,
        searchExamples: Boolean = false
    ) = when (detectedSearchScript) {
        // edge case: unable to detect search script
        SearchScript.AUTO -> dictionaryRepository.searchAll(
            positionedQuery = positionedQuery,
            simpleQuery = simpleQuery,
            searchDefinitions = searchDefinitions,
            searchExamples = searchExamples
        )

        SearchScript.SYLHETI_NAGRI -> dictionaryRepository.searchNagri(
            positionedQuery = positionedQuery,
            simpleQuery = simpleQuery,
            searchDefinitions = searchDefinitions,
            searchExamples = searchExamples
        )

        else -> detectedSearchScript.languages.filter { language ->
            with(settings) {
                script == SearchScript.AUTO || languages[language] == true
            }
        }.flatMap { language ->
            language.search(
                dictionaryRepository = dictionaryRepository,
                positionedQuery = positionedQuery,
                simpleQuery = simpleQuery,
                searchDefinitions = searchDefinitions,
                searchExamples = searchExamples
            )
        }
    }

    private suspend fun getSearchResults(
        globSearchTerm: String?,
        detectedSearchScript: SearchScript,
        settings: SearchSettingsState
    ) = globSearchTerm?.let {
        _searchState.update { it.copy(resultsLoading = true) }
        with(settings) {
            val positionedQuery = position.getPositionedQuery(it)
            Logger.d("SEARCH: getSearchResults() $positionedQuery")

            getQueryResults(
                positionedQuery = positionedQuery,
                simpleQuery = SearchPosition.ANYWHERE.getPositionedQuery(it),
                detectedSearchScript = detectedSearchScript,
                searchDefinitions = searchDefinitions,
                searchExamples = searchExamples,
                settings = settings
            ).distinct()
                .sortedWith(
                    compareBy(UnicodeUtility.SYLHETI_IPA_SORTER) {
                        it.displayIPA
                    }
                )
        }
    }

    private suspend fun refreshSearch(
        searchTerm: String,
        settings: SearchSettingsState
    ): Triple<Boolean, SearchScript, Regex> {
        val (globSearchTerm, detectedSearchScript, highlightRegex) = processSearchQuery(searchTerm, settings)
        val searchResults = getSearchResults(globSearchTerm, detectedSearchScript, settings)
        searchResultsStateFlow.update { searchResults }

        return Triple(
            !searchResults.isNullOrEmpty(),
            detectedSearchScript,
            highlightRegex
        )
    }

    private suspend fun getSearchSuggestions(
        searchTerm: String,
        settings: SearchSettingsState
    ) = coroutineScope {
        val (globSearchTerm, detectedSearchScript, highlightRegex) = processSearchQuery(searchTerm, settings)
        val suggestionQuery = globSearchTerm?.let(settings.position::getSuggestionQuery)

        Logger.d("SEARCH: getSearchSuggestions() $suggestionQuery")

        val recentSearchesJob = async {
            getRecentSearches(suggestionQuery, detectedSearchScript, highlightRegex)
        }
        val suggestionsJob = suggestionQuery?.let {
            async { getSuggestions(suggestionQuery, detectedSearchScript, highlightRegex, settings) }
        }

        val recentSearches = recentSearchesJob.await()
        val suggestions = suggestionsJob?.await()?.filter {
            it !in recentSearches
        } ?: emptyList()

        recentSearches to suggestions
    }

    private suspend fun getRecentSearches(
        suggestionQuery: String?,
        detectedSearchScript: SearchScript,
        highlightRegex: Regex
    ) = recentSearchesRepository.getRecentSearches(
        suggestionQuery = suggestionQuery,
        script = detectedSearchScript
    ).map { (term, script) ->
        SDString(
            text = term,
            highlightRegex = highlightRegex,
            script = script
        )
    }

    private suspend fun getSuggestions(
        suggestionQuery: String,
        detectedSearchScript: SearchScript,
        highlightRegex: Regex,
        settings: SearchSettingsState
    ): Set<SDString> {
        val suggestions = mutableSetOf<SDString>()

        getQueryResults(suggestionQuery, detectedSearchScript, settings).forEach { entry ->
            yield()
            with(entry) {
                when {
                    detectedSearchScript == SearchScript.EASTERN_NAGRI && displayBengali != null ->
                        suggestions += SDString(displayBengali!!, highlightRegex, SearchScript.EASTERN_NAGRI)

                    detectedSearchScript == SearchScript.SYLHETI_NAGRI && displayNagri != null ->
                        suggestions += SDString(displayNagri!!, highlightRegex)

                    else -> {
                        val isAuto = settings.script == SearchScript.AUTO
                        val searchLanguages = settings.languages

                        if (displayIPA.contains(highlightRegex) && (
                            isAuto || searchLanguages[SearchLanguage.Latin.SYLHETI] == true
                        )) {
                            suggestions += SDString(displayIPA, highlightRegex, SearchScript.LATIN)
                        }

                        if (gloss?.contains(highlightRegex) == true && (
                            isAuto || searchLanguages[SearchLanguage.Latin.ENGLISH] == true
                        )) {
                            suggestions += SDString(gloss, highlightRegex, SearchScript.LATIN)
                        }
                    }
                }
            }
        }

        return suggestions
    }
}
