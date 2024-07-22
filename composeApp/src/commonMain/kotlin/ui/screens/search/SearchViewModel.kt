package ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.dictionary.DictionaryDataSource
import data.favorites.FavoritesRepository
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchViewModel : ViewModel(), KoinComponent {

    private val dictionaryDataSource: DictionaryDataSource by inject()
    private val favoritesRepository: FavoritesRepository by inject()
    private val preferencesRepository: PreferencesRepository by inject()

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = combine(
        _state,
        favoritesRepository.getFavorites(),
    ) { state, favorites ->
        state.copy(
            favorites = favorites
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SearchState())

    private var searchJob: Job? = null

    fun onEvent(event: SearchEvent) {
        when (event) {
            SearchEvent.Search -> { setSearchBarActive(false) }
            is SearchEvent.SetSearchBarActive -> { setSearchBarActive(event.value) }
            is SearchEvent.UpdateSearchTerm -> { updateSearchTerm(event.value) }
            SearchEvent.ClearSearchBar -> {
                if (_state.value.searchTerm.isBlank()) {
                    setSearchBarActive(false)
                } else updateSearchTerm("")
            }

            is SearchEvent.SelectSuggestion -> {
                updateSearchTerm(event.value) { error ->
                    if (error == null) setSearchBarActive(false)
                }
            }

            is SearchEvent.MarkFavorite -> {
                viewModelScope.launch {
                    if (event.isFavorite) {
                        favoritesRepository.addFavorite(event.entryId)
                    } else {
                        favoritesRepository.removeFavorite(event.entryId)
                    }
                }
            }
        }
    }

    private fun setSearchBarActive(value: Boolean) {
        _state.update {
            it.copy(searchBarActive = value)
        }
    }

    private fun updateSearchTerm(term: String, onCompletion: CompletionHandler = {}) {
        searchJob?.cancel()
        _state.update {
            it.copy(searchTerm = term)
        }
        if (_state.value.searchTerm.isBlank()) {
            _state.update {
                it.copy(searchResults = null)
            }
        } else {
            searchJob = viewModelScope.launch {
                Logger.d("SEARCH: Searching for $term")
                val results = dictionaryDataSource.searchSylLatin("*$term*")
                Logger.d("SEARCH: Found ${results.size}")
                _state.update {
                    it.copy(searchResults = results)
                }
            }
            searchJob?.invokeOnCompletion(onCompletion)
        }
    }
}
