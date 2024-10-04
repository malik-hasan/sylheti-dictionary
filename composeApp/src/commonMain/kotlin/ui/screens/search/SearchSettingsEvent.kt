package ui.screens.search

sealed interface SearchSettingsEvent {
    data class ToggleSettingsMenu(val value: Boolean): SearchSettingsEvent
    data class SelectSearchPosition(val index: Int, val selected: Boolean): SearchSettingsEvent
}
