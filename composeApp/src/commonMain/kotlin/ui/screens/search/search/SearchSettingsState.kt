package ui.screens.search.search

import models.search.settings.SearchLanguage
import models.search.settings.SearchPosition
import models.search.settings.SearchScript

data class SearchSettingsState(
    val menuExpanded: Boolean = false,
    val position: SearchPosition = SearchPosition.START,
    val script: SearchScript = SearchScript.AUTO,
    val languages: Map<SearchLanguage, Boolean> = emptyMap(),
    val searchDefinitions: Boolean = false,
    val searchExamples: Boolean = false
)
