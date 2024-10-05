package ui

import androidx.lifecycle.ViewModel
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import models.EN
import ui.utils.stateFlowOf

class AppViewModel(val preferences: PreferencesRepository): ViewModel() {

    val locale = stateFlowOf(EN,
        preferences.flow(PreferenceKey.LOCALE, EN)
    )

    val dynamicTheme = stateFlowOf(true,
        preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
    )
}
