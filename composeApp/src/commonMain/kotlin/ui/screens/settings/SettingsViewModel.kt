package ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.launch
import utility.setLocale

class SettingsViewModel(private val preferences: PreferencesRepository): ViewModel() {

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.SetLocale -> {
                val languageCode = event.languageCode
                setLocale(languageCode)
                viewModelScope.launch {
                    preferences.put(PreferenceKey.LOCALE, languageCode)
                }
            }

            is SettingsEvent.ToggleDynamicTheme -> {
                viewModelScope.launch {
                    preferences.put(PreferenceKey.DYNAMIC_THEME, event.value)
                }
            }
        }
    }

}
