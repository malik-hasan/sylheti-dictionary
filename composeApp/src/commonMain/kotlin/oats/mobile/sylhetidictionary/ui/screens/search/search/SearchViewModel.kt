package oats.mobile.sylhetidictionary.ui.screens.search.search

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryRepository
import oats.mobile.sylhetidictionary.data.dictionary.models.displayEN
import oats.mobile.sylhetidictionary.data.dictionary.models.displayIPA
import oats.mobile.sylhetidictionary.data.dictionary.models.displaySN
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchLanguage
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchPosition
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchScript
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesRepository
import oats.mobile.sylhetidictionary.di.utils.injectLogger
import oats.mobile.sylhetidictionary.ui.models.SDString
import oats.mobile.sylhetidictionary.ui.utils.stateFlowOf
import oats.mobile.sylhetidictionary.utility.UnicodeUtility
import oats.mobile.sylhetidictionary.utility.getScrollCharIndexes
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinComponent
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.at_least_one_language

@OptIn(FlowPreview::class)
class SearchViewModel(
    processTextSearchTerm: String?,
    private val preferences: PreferencesRepository,
    private val dictionaryRepository: DictionaryRepository,
    private val bookmarksRepository: BookmarksRepository,
    private val recentSearchesRepository: RecentSearchesRepository,
) : ViewModel(), KoinComponent {

    private val logger: Logger by injectLogger()

    val snackbarHostState = SnackbarHostState()

    val settingsState = stateFlowOf(SearchSettingsState(),
        with(preferences) {
            combine(
                searchPosition,
                searchScript,
                searchLanguages,
                combine(
                    flow(PreferenceKey.SEARCH_DEFINITIONS, false),
                    flow(PreferenceKey.SEARCH_EXAMPLES, false),
                    ::Pair
                ),
                flow(PreferenceKey.FEATURE_BENGALI_EXAMPLES, false),
            ) { position, script, languages, (searchDefinitions, searchExamples), featureBengaliExamples ->
                SearchSettingsState(
                    position = position,
                    script = script,
                    languages = languages.filterKeys { it in script.languages },
                    searchDefinitions = searchDefinitions,
                    searchExamples = searchExamples,
                    featureBengaliExamples = featureBengaliExamples
                )
            }.distinctUntilChanged()
                .onEach {
                    logger.d("SEARCH: refreshing search for settings change $it")
                    refreshSearch(searchInputState.text.toString(), it)
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

            is SearchSettingsEvent.EnableSearchDefinitions -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_DEFINITIONS, event.enable)
            }

            is SearchSettingsEvent.EnableSearchExamples -> viewModelScope.launch {
                preferences.set(PreferenceKey.SEARCH_EXAMPLES, event.enable)
            }
        }
    }

    val searchQueryState = TextFieldState("")
    val searchInputState = TextFieldState(processTextSearchTerm ?: "")

    private val searchSuggestionsFlow = settingsState.combine(
        snapshotFlow { searchInputState.text }.debounce(300)
    ) { settings, searchTerm ->
        getSearchSuggestions(searchTerm.toString(), settings)
    }

    private val searchResultsSharedFlow = MutableSharedFlow<List<DictionaryEntry>?>()

    private val entriesFlow = searchResultsSharedFlow.combine(
        bookmarksRepository.bookmarksFlow
    ) { searchResults, bookmarks ->
        val entries = (searchResults ?: dictionaryRepository.getEntries(bookmarks)).sortedWith(
            compareBy(UnicodeUtility.SYLHETI_IPA_SORTER) { it.displayIPA }
        )

        entries to entries.getScrollCharIndexes()
    }

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = stateFlowOf(SearchState(),
        combine(
            _searchState,
            searchSuggestionsFlow,
            entriesFlow,
            preferences.flow(PreferenceKey.FEATURE_BENGALI_DEFINITIONS, false)
        ) { state, (recents, suggestions), (entries, scrollCharIndexes), featureBengaliDefinitions ->
            state.copy(
                recents = recents,
                suggestions = suggestions,
                entries = entries,
                scrollCharIndexes = scrollCharIndexes,
                featureBengaliDefinitions = featureBengaliDefinitions
            )
        }
    )

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OpenSettingsMenu -> _searchState.update {
                it.copy(settingsMenuOpen = event.open)
            }

            SearchEvent.Search -> search()
            is SearchEvent.SelectSuggestion -> {
                searchInputState.setTextAndPlaceCursorAtEnd(event.term)
                search()
            }

            is SearchEvent.ClearSearch -> viewModelScope.launch {
                if (!event.searchBarExpanded) {
                    logger.d("SEARCH: clearing results and highlight regex")
                    searchQueryState.clearText()
                    searchResultsSharedFlow.emit(null)
                    preferences.setHighlightRegex(Regex(""))
                } else searchInputState.clearText()
            }

            is SearchEvent.Bookmark -> with(event) {
                viewModelScope.launch {
                    with(bookmarksRepository) {
                        if (bookmark) {
                            addBookmark(entryId)
                        } else removeBookmark(entryId)
                    }
                }
            }
        }
    }

    private fun search() = viewModelScope.launch {
        searchInputState.text.let { searchInputText ->
            if (searchInputText != searchQueryState.text) {
                val searchTerm = searchInputText.toString()
                logger.d("SEARCH: searching for $searchTerm")

                searchQueryState.setTextAndPlaceCursorAtEnd(searchInputState.text.toString())
                val (hasSearchResults, detectedSearchScript, highlightRegex) = refreshSearch(searchTerm, settingsState.value)

                preferences.setHighlightRegex(highlightRegex)

                if (hasSearchResults) {
                    recentSearchesRepository.cacheSearch(searchTerm, detectedSearchScript)
                }
            }
        }
    }

    init {
        if (searchInputState.text.isNotEmpty()) search()
    }

    private suspend fun processSearchQuery(searchTerm: String, settings: SearchSettingsState) = coroutineScope {
        val globSearchTermJob = async {
            searchTerm.takeIf { it.isNotBlank() }?.let {
                mapAltChars(
                    term = mapAltChars(
                        term = escapeGlobChars(it),
                        charMap = UnicodeUtility.STOP_CHAR_MAP + UnicodeUtility.CASE_MAP
                    ),
                    charMap = UnicodeUtility.LATIN_IPA_CHAR_MAP,
                    remapped = true
                )
            }.also { logger.d("SEARCH: globSearchTerm: $it") }
        }

        val detectSearchScriptJob = async { detectSearchScript(searchTerm, settings.script) }

        val highlightRegexJob = async {
            val regexSearchTerm = if (searchTerm.isBlank()) {
                ""
            } else mapAltChars(
                term = Regex.escape(searchTerm.lowercase()),
                charMap = UnicodeUtility.STOP_CHAR_MAP + UnicodeUtility.LATIN_IPA_CHAR_MAP,
                forRegex = true
            )

            logger.d("SEARCH: highlight regex: $regexSearchTerm")
            Regex(regexSearchTerm, RegexOption.IGNORE_CASE)
        }

        Triple(
            globSearchTermJob.await(),
            detectSearchScriptJob.await(),
            highlightRegexJob.await()
        )
    }

    private suspend fun detectSearchScript(term: String, searchScriptPreference: SearchScript) = withContext(Dispatchers.Default) {
        if (searchScriptPreference == SearchScript.AUTO) {
            term.forEach { char ->
                ensureActive()
                SearchScript.entries.forEach { script ->
                    ensureActive()
                    script.regexCharSet?.let { regex ->
                        if (char.toString().matches(regex)) {
                            logger.d("SEARCH: detected script: $script from term: $term")
                            return@withContext script
                        }
                    }
                }
            }
            logger.d("SEARCH: unable to detect search script from term: $term")
        }
        searchScriptPreference
    }

    private suspend fun escapeGlobChars(term: String) = withContext(Dispatchers.Default) {
        term.map { char ->
            ensureActive()
            if (char in UnicodeUtility.GLOB_SPECIAL_CHARS) {
                "[$char]"
            } else char.toString()
        }.joinToString("")
    }

    private suspend fun mapAltChars(
        term: String,
        charMap: Map<Char, Set<Char>>,
        forRegex: Boolean = false,
        remapped: Boolean = false
    ) = withContext(Dispatchers.Default) {
        term.map { char ->
            ensureActive()
            charMap[char]?.let { altChars ->
                var charSet = "$char${altChars.joinToString("")}"
                if (!remapped) charSet = "[$charSet]"
                if (forRegex) charSet = "\\E$charSet\\Q" // escape the escaping
                charSet
            } ?: char.toString()
        }.joinToString("")
    }

    private suspend fun getQueryResults(
        positionedQuery: String,
        detectedSearchScript: SearchScript,
        settings: SearchSettingsState,
        simpleQuery: String = "",
        searchDefinitions: Boolean = false,
        searchExamples: Boolean = false
    ) = with(dictionaryRepository) {
        val search = when (detectedSearchScript) {
            SearchScript.AUTO -> ::searchAll // edge case: unable to detect search script
            SearchScript.SYLHETI_NAGRI -> ::searchSylhetiNagri
            SearchScript.EASTERN_NAGRI -> with(settings) {
                when {
                    script == SearchScript.AUTO || SearchScript.EASTERN_NAGRI.languages.all { languages[it] == true } -> ::searchEasternNagri
                    languages[SearchLanguage.EasternNagri.BENGALI] == true -> ::searchBengaliEasternNagri
                    languages[SearchLanguage.EasternNagri.SYLHETI] == true -> ::searchSylhetiEasternNagri
                    else -> { positionedQuery, simpleQuery, searchDefinitions, searchExamples ->
                        logger.e("SEARCH: searching Eastern Nagri with no languages enabled")
                        searchEasternNagri(positionedQuery, simpleQuery, searchDefinitions, searchExamples)
                    }
                }
            }

            SearchScript.LATIN -> with(settings) {
                when {
                    script == SearchScript.AUTO || SearchScript.LATIN.languages.all { languages[it] == true } -> ::searchLatin
                    languages[SearchLanguage.Latin.ENGLISH] == true -> ::searchEnglish
                    languages[SearchLanguage.Latin.SYLHETI] == true -> ::searchSylhetiLatin
                    else -> { positionedQuery, simpleQuery, searchDefinitions, searchExamples ->
                        logger.e("SEARCH: searching Latin with no languages enabled")
                        searchLatin(positionedQuery, simpleQuery, searchDefinitions, searchExamples)
                    }
                }
            }
        }

        search(positionedQuery, simpleQuery, searchDefinitions, searchExamples)
    }

    private suspend fun getSearchResults(
        globSearchTerm: String?,
        detectedSearchScript: SearchScript,
        settings: SearchSettingsState
    ) = globSearchTerm?.let {
        with(settings) {
            val positionedQuery = position.getPositionedQuery(it)
            logger.d("SEARCH: getSearchResults() $positionedQuery")

            getQueryResults(
                positionedQuery = positionedQuery,
                simpleQuery = SearchPosition.ANYWHERE.getPositionedQuery(it),
                detectedSearchScript = detectedSearchScript,
                searchDefinitions = searchDefinitions,
                searchExamples = searchExamples,
                settings = settings
            )
        }
    }

    val resultsListState = LazyListState()

    private suspend fun refreshSearch(
        searchTerm: String,
        settings: SearchSettingsState
    ): Triple<Boolean, SearchScript, Regex> {
        _searchState.update { it.copy(resultsLoading = true) }

        val (globSearchTerm, detectedSearchScript, highlightRegex) = processSearchQuery(searchTerm, settings)
        val searchResults = getSearchResults(globSearchTerm, detectedSearchScript, settings)
        logger.d("SEARCH: found ${searchResults?.size ?: "no"} results")

        searchResultsSharedFlow.emit(searchResults)
        _searchState.update { it.copy(resultsLoading = false) }
        resultsListState.requestScrollToItem(0)

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

        logger.d("SEARCH: getSearchSuggestions() $suggestionQuery")

        val recentSearchesJob = async { getRecentSearches(suggestionQuery, detectedSearchScript, highlightRegex) }
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
    ) = withContext(Dispatchers.Default) {
        val suggestions = mutableSetOf<SDString>()

        getQueryResults(suggestionQuery, detectedSearchScript, settings).forEach { entry ->
            ensureActive()
            with(entry) {
                when (detectedSearchScript) {
                    SearchScript.EASTERN_NAGRI if displayEN != null ->
                        suggestions += SDString(displayEN!!, highlightRegex, SearchScript.EASTERN_NAGRI)

                    SearchScript.SYLHETI_NAGRI if displaySN != null ->
                        suggestions += SDString(displaySN!!, highlightRegex)

                    else -> {
                        val isAuto = settings.script == SearchScript.AUTO
                        val searchLanguages = settings.languages

                        if (displayIPA.contains(highlightRegex)
                            && (isAuto || searchLanguages[SearchLanguage.Latin.SYLHETI] == true)
                        ) {
                            suggestions += SDString(displayIPA, highlightRegex, SearchScript.LATIN)
                        }

                        if (gloss?.contains(highlightRegex) == true
                            && (isAuto || searchLanguages[SearchLanguage.Latin.ENGLISH] == true)
                        ) {
                            suggestions += SDString(gloss, highlightRegex, SearchScript.LATIN)
                        }
                    }
                }
            }
        }

        suggestions
    }
}
