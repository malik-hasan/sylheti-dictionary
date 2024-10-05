package ui

import androidx.lifecycle.ViewModel
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import models.Language
import ui.utils.stateFlowOf

class AppViewModel(val preferences: PreferencesRepository): ViewModel() {

    val language = stateFlowOf(Language.EN, preferences.languageFlow)

    val dynamicTheme = stateFlowOf(true,
        preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
    )
}
