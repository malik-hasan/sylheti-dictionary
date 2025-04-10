package oats.mobile.sylhetidictionary.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.settings.PreferenceKey
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.ui.utils.blockingStateFlowOf
import oats.mobile.sylhetidictionary.utility.refreshLanguage

class AppViewModel(val preferences: PreferencesRepository): ViewModel() {

    val language = blockingStateFlowOf(preferences.language)

    fun refreshLanguage() {
        viewModelScope.launch {
            preferences.refreshLanguage()
        }
    }

    val theme = blockingStateFlowOf(preferences.theme)

    val dynamicTheme = blockingStateFlowOf(
        preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
    )
}
