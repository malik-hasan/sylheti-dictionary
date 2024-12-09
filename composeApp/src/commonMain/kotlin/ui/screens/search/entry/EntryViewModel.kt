package ui.screens.search.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.bookmarks.BookmarksDataSource
import data.dictionary.DictionaryDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ui.utils.stateFlowOf

class EntryViewModel(
    private val entryId: String,
    dictionaryDataSource: DictionaryDataSource,
    private val bookmarksDataSource: BookmarksDataSource
) : ViewModel() {

    private val initialEntryState = with(dictionaryDataSource) {
        EntryState(
            entry = getEntry(entryId),
            examples = getExamples(entryId)
        )
    }

    private val _state = MutableStateFlow(initialEntryState)
    val state = stateFlowOf(initialEntryState,
        combine(
            _state,
            bookmarksDataSource.isBookmarkFlow(entryId),
        ) { state, isBookmark ->
            state.copy(isBookmark = isBookmark)
        }
    )

    fun onEvent(event: EntryEvent) {
        when(event) {
            is EntryEvent.ToggleBookmark -> viewModelScope.launch {
                with(bookmarksDataSource) {
                    if (state.value.isBookmark) {
                        removeBookmark(entryId)
                    } else addBookmark(entryId)
                }
            }
        }
    }
}
