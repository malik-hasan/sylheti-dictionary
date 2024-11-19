package ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesDataSource
import kotlinx.coroutines.launch
import utility.setAppOSLanguage

class SettingsViewModel(private val preferences: PreferencesDataSource): ViewModel() {

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
        }
    }

}
