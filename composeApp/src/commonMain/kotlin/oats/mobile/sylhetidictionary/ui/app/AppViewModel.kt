package oats.mobile.sylhetidictionary.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.settings.PreferenceKey
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.models.settings.Language
import oats.mobile.sylhetidictionary.models.settings.Theme
import oats.mobile.sylhetidictionary.ui.utils.stateFlowOf
import oats.mobile.sylhetidictionary.utility.refreshLanguage

class AppViewModel(val preferences: PreferencesRepository): ViewModel() {

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
}
