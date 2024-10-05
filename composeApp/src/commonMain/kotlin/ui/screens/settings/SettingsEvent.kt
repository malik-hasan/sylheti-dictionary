package ui.screens.settings

import models.Language

sealed interface SettingsEvent {
    data class SetLanguage(val language: Language) : SettingsEvent
    data class ToggleDynamicTheme(val value: Boolean) : SettingsEvent
}
