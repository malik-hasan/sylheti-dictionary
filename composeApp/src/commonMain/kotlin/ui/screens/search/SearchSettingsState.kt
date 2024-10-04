package ui.screens.search

data class SearchSettingsState(
    val menuExpanded: Boolean = false,
    val searchPositions: List<Boolean> = List(3) { false }
)
