package ui.screens.settings

sealed interface SettingsEvent {
    data class SetLocale(val languageCode: String) : SettingsEvent
    data class ToggleDynamicTheme(val value: Boolean) : SettingsEvent
}