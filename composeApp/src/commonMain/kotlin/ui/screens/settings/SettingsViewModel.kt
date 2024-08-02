package ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import di.setLanguage
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: PreferencesRepository): ViewModel() {

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.SetLocale -> {
                setLanguage(event.locale.code) {
                    Logger.d("SETTINGS: Saving locale to preferences")
                    viewModelScope.launch {
                        preferences.put(PreferenceKey.LOCALE, event.locale.code)
                    }
                }
            }
        }
    }

}
