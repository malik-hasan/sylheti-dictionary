package ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import di.setLanguage
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: PreferencesRepository): ViewModel() {

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.SetLocale -> {
                val languageCode = event.locale.code
                setLanguage(languageCode)
                viewModelScope.launch {
                    preferences.put(PreferenceKey.LOCALE, languageCode)
                }
            }
        }
    }

}
