package oats.mobile.sylhetidictionary.models

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Search: Route {
        const val ACTIVATE_SEARCH_BAR_KEY = "ACTIVATE_SEARCH_BAR"
    }

    @Serializable data object Settings: Route
    @Serializable data object IpaHelp: Route
    @Serializable data object About: Route
    @Serializable data class Entry(val entryId: String): Route
}
