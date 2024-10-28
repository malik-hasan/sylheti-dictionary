package ui.screens.search

import models.search.settings.SearchLanguage
import models.search.settings.SearchScript

data class SearchSettingsState(
    val menuExpanded: Boolean = false,
    val positions: List<Boolean> = List(4) { false },
    val script: SearchScript = SearchScript.AUTO,
    val languages: Map<SearchLanguage, Boolean> = emptyMap()
)
