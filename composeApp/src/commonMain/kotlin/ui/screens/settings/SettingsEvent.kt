package ui.screens.settings

import models.Locale

sealed interface SettingsEvent {
    data class SetLocale(val locale: Locale) : SettingsEvent
}
