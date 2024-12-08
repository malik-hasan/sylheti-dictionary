package ui.screens.search.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.bookmarks.BookmarksDataSource
import data.dictionary.DictionaryDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.screens.settings.SettingsEvent

class EntryViewModel(
    entryId: String,
    dictionaryDataSource: DictionaryDataSource,
    bookmarksDataSource: BookmarksDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(EntryState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                with(dictionaryDataSource) {
                    EntryState(
                        entry = getEntry(entryId),
                        isBookmark = bookmarksDataSource.checkBookmark(entryId),
                        examples = getExamples(entryId)
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
//        when(event) {
//            is SettingsEvent.SetLanguage -> with(event) {
//                viewModelScope.launch {
//                    setAppOSLanguage(language)
//                    preferences.setLanguage(language)
//                }
//            }
//
//            is SettingsEvent.SelectTheme -> viewModelScope.launch {
//                preferences.set(PreferenceKey.THEME, event.theme.ordinal)
//            }
//
//            is SettingsEvent.ToggleDynamicTheme -> viewModelScope.launch {
//                preferences.set(PreferenceKey.DYNAMIC_THEME, !state.value.dynamicThemeEnabled)
//            }
//
//            is SettingsEvent.ToggleShowNagri -> viewModelScope.launch {
//                preferences.set(PreferenceKey.SHOW_NAGRI, !state.value.showNagriEnabled)
//                if (preferences.get(PreferenceKey.SEARCH_SCRIPT) == SearchScript.NAGRI.ordinal) {
//                    preferences.set(PreferenceKey.SEARCH_SCRIPT, 0)
//                }
//            }
//        }
    }
}
