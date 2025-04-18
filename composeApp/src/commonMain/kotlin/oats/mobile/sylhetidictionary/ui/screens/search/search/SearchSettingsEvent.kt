package oats.mobile.sylhetidictionary.ui.screens.search.search

import oats.mobile.sylhetidictionary.models.search.settings.SearchLanguage
import oats.mobile.sylhetidictionary.models.search.settings.SearchPosition
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript

sealed interface SearchSettingsEvent {
    data class SelectPosition(val position: SearchPosition): SearchSettingsEvent
    data class SelectScript(val script: SearchScript): SearchSettingsEvent
    data class SelectLanguage(val language: SearchLanguage, val selected: Boolean): SearchSettingsEvent
    data class EnableSearchDefinitions(val enable: Boolean): SearchSettingsEvent
    data class EnableSearchExamples(val enable: Boolean): SearchSettingsEvent
}
