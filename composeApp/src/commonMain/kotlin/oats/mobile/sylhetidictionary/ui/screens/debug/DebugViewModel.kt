package oats.mobile.sylhetidictionary.ui.screens.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Language
import oats.mobile.sylhetidictionary.ui.utils.stateFlowOf
import oats.mobile.sylhetidictionary.utility.setAppOSLanguage

class DebugViewModel(private val preferences: PreferencesRepository): ViewModel() {

    val state = stateFlowOf(DebugState(),
        combine(
            preferences.flow(PreferenceKey.FEATURE_BENGALI_APP_LOCALE, false),
            preferences.flow(PreferenceKey.FEATURE_BENGALI_DEFINITIONS, false),
            preferences.flow(PreferenceKey.FEATURE_BENGALI_EXAMPLES, false)
        ) { featureBengaliAppLocale, featureBengaliDefinitions, featureBengaliExamples ->
            DebugState(
                featureBengaliAppLocale = featureBengaliAppLocale,
                featureBengaliDefinitions = featureBengaliDefinitions,
                featureBengaliExamples = featureBengaliExamples
            )
        }
    )

    fun onEvent(event: DebugEvent) {
        when (event) {
            is DebugEvent.EnableFeatureBengaliAppLocale -> viewModelScope.launch {
                preferences.set(PreferenceKey.FEATURE_BENGALI_APP_LOCALE, event.enable)
                setAppOSLanguage(Language.EN)
                preferences.setLanguage(Language.EN)
            }

            is DebugEvent.EnableFeatureBengaliDefinitions -> viewModelScope.launch {
                preferences.set(PreferenceKey.FEATURE_BENGALI_DEFINITIONS, event.enable)
            }

            is DebugEvent.EnableFeatureBengaliExamples -> viewModelScope.launch {
                preferences.set(PreferenceKey.FEATURE_BENGALI_EXAMPLES, event.enable)
            }
        }
    }
}
