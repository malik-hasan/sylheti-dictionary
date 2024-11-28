package ui.screens.settings

import models.Language

sealed interface SettingsEvent {
    data class SetLanguage(val language: Language) : SettingsEvent
    data object ToggleDynamicTheme : SettingsEvent
    data object ToggleShowNagri : SettingsEvent
}
