package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import models.Locale

class AppViewModel(preferences: PreferencesRepository): ViewModel() {

    val locale = preferences.flow(PreferenceKey.LOCALE, Locale.English.code)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Locale.English.code)

}
