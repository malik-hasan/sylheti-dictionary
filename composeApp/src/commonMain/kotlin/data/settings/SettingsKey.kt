package data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey

object SettingsKey {
    val SEARCH_BEGINNING = booleanPreferencesKey("search_beginning")
    val SEARCH_MIDDLE = booleanPreferencesKey("search_middle")
    val SEARCH_END = booleanPreferencesKey("search_end")
}
