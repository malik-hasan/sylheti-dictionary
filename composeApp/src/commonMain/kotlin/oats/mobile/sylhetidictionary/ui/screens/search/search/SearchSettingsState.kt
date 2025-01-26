package oats.mobile.sylhetidictionary.ui.screens.search.search

import oats.mobile.sylhetidictionary.models.search.settings.SearchLanguage
import oats.mobile.sylhetidictionary.models.search.settings.SearchPosition
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript

data class SearchSettingsState(
    val position: SearchPosition = SearchPosition.START,
    val script: SearchScript = SearchScript.AUTO,
    val languages: Map<SearchLanguage, Boolean> = emptyMap(),
    val searchDefinitions: Boolean = false,
    val searchExamples: Boolean = false
)
