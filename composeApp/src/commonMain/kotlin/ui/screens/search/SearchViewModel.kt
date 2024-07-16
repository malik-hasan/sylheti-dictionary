package ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.dictionary.DictionaryDataSource
import data.favorites.FavoritesRepository
import data.settings.PreferencesRepository
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchViewModel : ViewModel(), KoinComponent {

//    private val dictionaryDao: DictionaryDao by inject()
    private val dictionaryDataSource: DictionaryDataSource by inject()
    private val favoritesRepository: FavoritesRepository by inject()
    private val preferencesRepository: PreferencesRepository by inject()

    var state by mutableStateOf(SearchState())
        private set

    private var searchJob: Job? = null

    fun onEvent(event: SearchEvent) {
        when (event) {
            SearchEvent.Search -> { setSearchBarActive(false) }
            SearchEvent.LoadFavorites -> { loadFavorites() }
            is SearchEvent.SetSearchBarActive -> { setSearchBarActive(event.value) }
            is SearchEvent.UpdateSearchTerm -> { updateSearchTerm(event.value) }
            SearchEvent.ClearSearchBar -> {
                if (state.searchTerm.isBlank()) {
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
//                        favoritesRepository.addFavorite(event.entryId)
                    } else {
//                        favoritesRepository.removeFavorite(event.entryId)
                    }
                }
            }
        }
    }

    private fun setSearchBarActive(value: Boolean) {
        state = state.copy(searchBarActive = value)
    }

    private fun updateSearchTerm(term: String, onCompletion: CompletionHandler = {}) {
        searchJob?.cancel()
        state = state.copy(searchTerm = term)
        if (state.searchTerm.isBlank()) {
            state = state.copy(searchResults = null)
        } else {
            searchJob = viewModelScope.launch {
                val results = dictionaryDataSource.searchSylLatin(term)
//                val results = dictionaryDao.searchSylLatin(
//                    SimpleSQLiteQuery(
//                        """SELECT * FROM DictionaryEntry
//                            |WHERE lexeme_ipa REGEXP ? OR
//                            |citation_ipa REGEXP ? OR
//                            |definition_ipa REGEXP ?
//                            |""".trimMargin(),
//                        arrayOf(term, term, term)
//                    )
//                )
                state = state.copy(searchResults = results)
            }
            searchJob?.invokeOnCompletion(onCompletion)
        }
    }

    private fun loadFavorites() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
//            val favorites = favoritesRepository.getFavorites()
//            state = state.copy(favorites = favorites)
        }
    }
}
