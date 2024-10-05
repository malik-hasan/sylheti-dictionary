package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.launch
import models.Language
import ui.utils.stateFlowOf
import utility.refreshLanguage

class AppViewModel(val preferences: PreferencesRepository): ViewModel() {

    val language = stateFlowOf(Language.EN, preferences.language)

    fun refreshLanguage() {
        viewModelScope.launch {
            preferences.refreshLanguage()
        }
    }

    val dynamicTheme = stateFlowOf(true,
        preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
    )
}
