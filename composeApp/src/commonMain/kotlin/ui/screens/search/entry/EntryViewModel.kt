package ui.screens.search.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.bookmarks.BookmarksDataSource
import data.dictionary.DictionaryDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.CardEntry
import models.toDictionaryEntry
import oats.mobile.sylhetidictionary.VariantEntry
import ui.utils.stateFlowOf

class EntryViewModel(
    private val entryId: String,
    private val dictionaryDataSource: DictionaryDataSource,
    private val bookmarksDataSource: BookmarksDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(EntryState())

    init {
        viewModelScope.launch {
            with(dictionaryDataSource) {
                val entry = getEntry(entryId)
                _state.update {
                    it.copy(entry = entry)
                }

                val variantEntries = async { getVariantEntries(entryId) }
                val variants = async { getVariants(entryId) }
                val examples = async { getExamples(entryId) }
                val componentLexemes = async {
                    getComponentLexemes(entryId).mapNotNull { componentEntry ->
                        var componentVariantEntries = emptyList<VariantEntry>()
                        if (componentEntry.definitionEN.isNullOrBlank()) {
                            componentVariantEntries = getVariantEntries(componentEntry.entryId)
                            if (componentVariantEntries.isEmpty()) return@mapNotNull null
                        }

                        componentEntry to CardEntry(
                            dictionaryEntry = componentEntry.toDictionaryEntry(),
                            isBookmark = false,
                            variantEntries = componentVariantEntries
                        )
                    }.toMap()
                }

                val relatedEntries = async {
                    entry.senseId?.let { senseId ->
                        getRelatedEntries(senseId).mapNotNull { relatedEntry ->
                            var relatedVariantEntries = emptyList<VariantEntry>()
                            if (relatedEntry.definitionEN.isNullOrBlank()) {
                                relatedVariantEntries = getVariantEntries(relatedEntry.entryId)
                                if (relatedVariantEntries.isEmpty()) return@mapNotNull null
                            }

                            relatedEntry to CardEntry(
                                dictionaryEntry = relatedEntry.toDictionaryEntry(),
                                isBookmark = false,
                                variantEntries = relatedVariantEntries
                            )
                        }.toMap()
                    } ?: emptyMap()
                }

                _state.update {
                    it.copy(
                        variantEntries = variantEntries.await(),
                        variants = variants.await(),
                        examples = examples.await(),
                        componentLexemes = componentLexemes.await(),
                        relatedEntries = relatedEntries.await()
                    )
                }
            }
        }
    }

    val state = stateFlowOf(EntryState(),
        _state.combine(
            bookmarksDataSource.bookmarksFlow
        ) { state, bookmarks ->
            state.copy(
                isBookmark = entryId in bookmarks,
                componentLexemes = state.componentLexemes.mapValues { (componentEntry, cardEntry) ->
                    cardEntry.copy(
                        isBookmark = componentEntry.entryId in bookmarks
                    )
                },
                relatedEntries = state.relatedEntries.mapValues { (relatedEntry, cardEntry) ->
                    cardEntry.copy(
                        isBookmark = relatedEntry.entryId in bookmarks
                    )
                }
            )
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
