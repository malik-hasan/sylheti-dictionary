package ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: PreferencesRepository): ViewModel() {

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.SetLanguage -> with(event) {
                viewModelScope.launch {
                    preferences.setLanguagePrefAndOS(language)
                }
            }

            is SettingsEvent.ToggleDynamicTheme -> {
                viewModelScope.launch {
                    preferences.set(PreferenceKey.DYNAMIC_THEME, event.value)
                }
            }
        }
    }

}
