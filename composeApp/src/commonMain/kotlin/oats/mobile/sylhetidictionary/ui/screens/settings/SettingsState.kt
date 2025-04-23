package oats.mobile.sylhetidictionary.ui.screens.settings

import oats.mobile.sylhetidictionary.data.preferences.models.settings.Theme

data class SettingsState(
    val theme: Theme = Theme.Auto,
    val dynamicThemeEnabled: Boolean = true
)
