package oats.mobile.sylhetidictionary.ui.screens.search.search

import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchLanguage
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchPosition
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchScript

data class SearchSettingsState(
    val position: SearchPosition = SearchPosition.START,
    val script: SearchScript = SearchScript.AUTO,
    val languages: Map<SearchLanguage, Boolean> = emptyMap(),
    val searchDefinitions: Boolean = false,
    val searchExamples: Boolean = false,
    val featureBengaliDefinitions: Boolean = false,
    val featureBengaliExamples: Boolean = false
)
