package oats.mobile.sylhetidictionary.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.ui.utils.stateFlowOf
import oats.mobile.sylhetidictionary.utility.setAppOSLanguage

class SettingsViewModel(private val preferences: PreferencesRepository): ViewModel() {

    val state = stateFlowOf(SettingsState(),
        combine(
            preferences.theme,
            preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
        ) { theme, dynamicTheme ->
            SettingsState(
                theme = theme,
                dynamicThemeEnabled = dynamicTheme
            )
        }
    )

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.SetLanguage -> with(event) {
                viewModelScope.launch {
                    setAppOSLanguage(language)
                    preferences.setLanguage(language)
                }
            }

            is SettingsEvent.SelectTheme -> viewModelScope.launch {
                preferences.set(PreferenceKey.THEME, event.theme.ordinal)
            }

            is SettingsEvent.EnableDynamicTheme -> viewModelScope.launch {
                preferences.set(PreferenceKey.DYNAMIC_THEME, event.enable)
            }
        }
    }
}
