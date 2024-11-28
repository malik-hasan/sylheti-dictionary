package ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesDataSource
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import models.search.settings.SearchScript
import ui.utils.stateFlowOf
import utility.setAppOSLanguage

class SettingsViewModel(private val preferences: PreferencesDataSource): ViewModel() {

    val settingsState = stateFlowOf(SettingsState(),
        combine(
            preferences.flow(PreferenceKey.DYNAMIC_THEME, true),
            preferences.flow(PreferenceKey.SHOW_NAGRI, false)
        ) { dynamicTheme, showNagri ->
            SettingsState(
                dynamicThemeEnabled = dynamicTheme,
                showNagriEnabled = showNagri
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

            is SettingsEvent.ToggleDynamicTheme -> viewModelScope.launch {
                preferences.set(PreferenceKey.DYNAMIC_THEME, event.value)
            }

            is SettingsEvent.ToggleShowNagri -> viewModelScope.launch {
                preferences.set(PreferenceKey.SHOW_NAGRI, event.value)
                if (preferences.get(PreferenceKey.SEARCH_SCRIPT) == SearchScript.NAGRI.ordinal) {
                    preferences.set(PreferenceKey.SEARCH_SCRIPT, 0)
                }
            }
        }
    }
}
