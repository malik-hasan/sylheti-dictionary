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
                val enabled = event.enable
                preferences.set(PreferenceKey.FEATURE_BENGALI_DEFINITIONS, enabled)

                if (!enabled && preferences.get(PreferenceKey.FEATURE_BENGALI_EXAMPLES) != true) {
                    turnOffBengaliSearchLanguage()
                }
            }

            is DebugEvent.EnableFeatureBengaliExamples -> viewModelScope.launch {
                val enabled = event.enable
                preferences.set(PreferenceKey.FEATURE_BENGALI_EXAMPLES, enabled)

                if (!enabled && preferences.get(PreferenceKey.FEATURE_BENGALI_DEFINITIONS) != true) {
                    turnOffBengaliSearchLanguage()
                }
            }
        }
    }

    private suspend fun turnOffBengaliSearchLanguage() {
        preferences.set(PreferenceKey.BENGALI_SEARCH_LANGUAGE, false)
        preferences.set(PreferenceKey.EASTERN_NAGRI_SCRIPT_SYLHETI_SEARCH_LANGUAGE, true)
    }
}
