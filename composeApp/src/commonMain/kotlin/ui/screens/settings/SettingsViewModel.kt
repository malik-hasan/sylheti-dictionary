package ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesDataSource
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ui.utils.stateFlowOf
import utility.setAppOSLanguage

class SettingsViewModel(private val preferences: PreferencesDataSource): ViewModel() {

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

            is SettingsEvent.ToggleDynamicTheme -> viewModelScope.launch {
                preferences.set(PreferenceKey.DYNAMIC_THEME, !state.value.dynamicThemeEnabled)
            }
        }
    }
}
