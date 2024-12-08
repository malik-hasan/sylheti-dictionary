package ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesDataSource
import kotlinx.coroutines.launch
import models.settings.Language
import models.settings.Theme
import ui.utils.stateFlowOf
import utility.refreshLanguage

class AppViewModel(val preferences: PreferencesDataSource): ViewModel() {

    val language = stateFlowOf(Language.EN, preferences.language)

    fun refreshLanguage() {
        viewModelScope.launch {
            preferences.refreshLanguage()
        }
    }

    val theme = stateFlowOf(Theme.Auto, preferences.theme)

    val dynamicTheme = stateFlowOf(true,
        preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
    )

    val showNagri = stateFlowOf(false,
        preferences.flow(PreferenceKey.SHOW_NAGRI, false)
    )

    val highlightRegex = stateFlowOf("",
        preferences.flow(PreferenceKey.HIGHLIGHT_REGEX, "")
    )

    val mappedIpaHighlightRegex = stateFlowOf("",
        preferences.flow(PreferenceKey.MAPPED_IPA_HIGHLIGHT_REGEX, "")
    )
}
