package ui.screens.settings

import models.settings.Language
import models.settings.Theme

sealed interface SettingsEvent {
    data class SetLanguage(val language: Language) : SettingsEvent
    data class SelectTheme(val theme: Theme) : SettingsEvent
    data object ToggleDynamicTheme : SettingsEvent
    data object ToggleShowNagri : SettingsEvent
}
