package oats.mobile.sylhetidictionary.ui.screens.settings

import oats.mobile.sylhetidictionary.models.settings.Language
import oats.mobile.sylhetidictionary.models.settings.Theme

sealed interface SettingsEvent {
    data class SetLanguage(val language: Language) : SettingsEvent
    data class SelectTheme(val theme: Theme) : SettingsEvent
    data class EnableDynamicTheme(val enable: Boolean) : SettingsEvent
}
