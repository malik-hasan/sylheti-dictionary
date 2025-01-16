package ui.screens.search.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.bookmarks.BookmarksDataSource
import data.dictionary.DictionaryDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import models.CardEntry
import models.toDictionaryEntry
import oats.mobile.sylhetidictionary.VariantEntry
import ui.utils.stateFlowOf

class EntryViewModel(
    private val entryId: String,
    dictionaryDataSource: DictionaryDataSource,
    private val bookmarksDataSource: BookmarksDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(EntryState())

    val state = stateFlowOf(EntryState(),
        combine(
            _state,
            bookmarksDataSource.bookmarksFlow
        ) { state, bookmarks ->
            with(dictionaryDataSource) {
                val entry = getEntry(entryId)
                state.copy(
                    entry = entry,
                    isBookmark = entryId in bookmarks,
                    examples = getExamples(entryId),
                    variants = getVariants(entryId),
                    variantEntries = getVariantEntries(entryId),
                    componentLexemes = getComponentLexemes(entryId).mapNotNull {
                        var variantEntries = emptyList<VariantEntry>()
                        if (it.definitionEN.isNullOrBlank()) {
                            variantEntries = getVariantEntries(it.entryId)
                            if (variantEntries.isEmpty()) return@mapNotNull null
                        }

                        it to CardEntry(
                            dictionaryEntry = it.toDictionaryEntry(),
                            isBookmark = it.entryId in bookmarks,
                            variantEntries = variantEntries
                        )
                    }.toMap(),
                    relatedEntries = entry.senseId?.let { senseId ->
                        getRelatedEntries(senseId).mapNotNull {
                            var variantEntries = emptyList<VariantEntry>()
                            if (it.definitionEN.isNullOrBlank()) {
                                variantEntries = getVariantEntries(it.entryId)
                                if (variantEntries.isEmpty()) return@mapNotNull null
                            }

                            it to CardEntry(
                                dictionaryEntry = it.toDictionaryEntry(),
                                isBookmark = it.entryId in bookmarks,
                                variantEntries = variantEntries
                            )
                        }.toMap()
                    } ?: emptyMap()
                )
            }
        }
    )

    fun onEvent(event: EntryEvent) {
        when(event) {
            is EntryEvent.Bookmark -> with(event) {
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
}
