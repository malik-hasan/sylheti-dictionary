package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import models.EN

class AppViewModel(val preferences: PreferencesRepository): ViewModel() {

    val locale = preferences.flow(PreferenceKey.LOCALE, EN)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), EN)

    val dynamicTheme = preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)
}
