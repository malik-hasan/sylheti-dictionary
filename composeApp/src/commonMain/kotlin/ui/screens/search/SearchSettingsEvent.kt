package ui.screens.search

import models.search.settings.SearchLanguage
import models.search.settings.SearchPosition
import models.search.settings.SearchScript

sealed interface SearchSettingsEvent {
    data class ToggleSettingsMenu(val value: Boolean): SearchSettingsEvent
    data class SelectPosition(val position: SearchPosition): SearchSettingsEvent
    data class SelectScript(val script: SearchScript): SearchSettingsEvent
    data class SelectLanguage(val language: SearchLanguage, val selected: Boolean): SearchSettingsEvent
}
