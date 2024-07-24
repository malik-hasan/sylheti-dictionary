package data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object PreferenceKey {
    val CURRENT_DICTIONARY_VERSION = intPreferencesKey("dictionary_version")
    val SEARCH_BEGINNING = booleanPreferencesKey("search_beginning")
    val SEARCH_MIDDLE = booleanPreferencesKey("search_middle")
    val SEARCH_END = booleanPreferencesKey("search_end")
}
