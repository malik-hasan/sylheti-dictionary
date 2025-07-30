package oats.mobile.sylhetidictionary.ui.screens.search.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryRepository
import oats.mobile.sylhetidictionary.ui.utils.stateFlowOf

class EntryViewModel(
    private val entryId: String,
    private val dictionaryRepository: DictionaryRepository,
    private val bookmarksRepository: BookmarksRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EntryState())

    init {
        viewModelScope.launch {
            with(dictionaryRepository) {
                val entry = getEntry(entryId)
                _state.update { it.copy(entry = entry) }

                val referenceEntries = async { getReferenceEntries(entryId) }
                val variants = async { getVariants(entryId) }
                val examples = async { getExamples(entryId) }
                val componentLexemes = async { getComponentLexemes(entryId) }
                val derivativeLexemes = async { getDerivativeLexemes(entryId) }
                val relatedEntries = async {
                    entry.senseId?.let { getRelatedEntries(it) } ?: emptyList()
                }

                _state.update {
                    it.copy(
                        referenceEntries = referenceEntries.await(),
                        variants = variants.await(),
                        examples = examples.await(),
                        componentEntries = componentLexemes.await(),
                        derivativeEntries = derivativeLexemes.await(),
                        relatedEntries = relatedEntries.await()
                    )
                }
            }
        }
    }

    val state = stateFlowOf(EntryState(),
        _state.combine(
            bookmarksRepository.isBookmarkFlow(entryId)
        ) { state, isBookmark ->
            state.copy(isBookmark = isBookmark)
        }
    )

    fun onEvent(event: EntryEvent) {
        when(event) {
            is EntryEvent.Bookmark -> with(event) {
                viewModelScope.launch {
                    with(bookmarksRepository) {
                        if (bookmark) {
                            addBookmark(entryId)
                        } else removeBookmark(entryId)
                    }
                }
            }

            is EntryEvent.ToggleVariants -> _state.update { it.copy(variantsExpanded = !it.variantsExpanded) }
            is EntryEvent.ToggleExamples -> _state.update { it.copy(examplesExpanded = !it.examplesExpanded) }
            is EntryEvent.ToggleComponentLexemes -> _state.update { it.copy(componentEntriesExpanded = !it.componentEntriesExpanded) }
            is EntryEvent.ToggleDerivativeLexemes -> _state.update { it.copy(derivativeEntriesExpanded = !it.derivativeEntriesExpanded) }
            is EntryEvent.ToggleRelatedWords -> _state.update { it.copy(relatedEntriesExpanded = !it.relatedEntriesExpanded) }
        }
    }
}
