package ui.screens.settings

import models.settings.Theme

data class SettingsState(
    val theme: Theme = Theme.Auto,
    val dynamicThemeEnabled: Boolean = true
)
