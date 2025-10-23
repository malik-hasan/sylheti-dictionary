package oats.mobile.sylhetidictionary.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.ui.utils.blockingStateFlowOf
import oats.mobile.sylhetidictionary.ui.utils.stateFlowOf
import oats.mobile.sylhetidictionary.utility.refreshLanguage

class AppViewModel(val preferences: PreferencesRepository): ViewModel() {

    val language = blockingStateFlowOf(preferences.language)

    fun refreshLanguage() {
        viewModelScope.launch {
            preferences.refreshLanguage()
        }
    }

    val featureBengaliDictionaryData = stateFlowOf(false,
        preferences.flow(PreferenceKey.FEATURE_BENGALI_DICTIONARY_DATA, false)
    )

    val theme = blockingStateFlowOf(preferences.theme)

    val dynamicTheme = blockingStateFlowOf(
        preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
    )
}
