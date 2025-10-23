package oats.mobile.sylhetidictionary.ui.screens.settings

import oats.mobile.sylhetidictionary.data.preferences.models.settings.Language
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Theme

sealed interface SettingsEvent {
    data class SetLanguage(val language: Language) : SettingsEvent
    data class SelectTheme(val theme: Theme) : SettingsEvent
    data class EnableDynamicTheme(val enable: Boolean) : SettingsEvent
    data class EnableBengaliDictionaryDataFeature(val enable: Boolean) : SettingsEvent
}
