package data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKey {
    val CURRENT_DICTIONARY_VERSION = intPreferencesKey("CURRENT_DICTIONARY_VERSION")

    val SEARCH_POSITION = intPreferencesKey("SEARCH_POSITION")
    val SEARCH_SCRIPT = intPreferencesKey("SEARCH_SCRIPT")
    val LATIN_SCRIPT_ENGLISH = booleanPreferencesKey("LATIN_SCRIPT_ENGLISH")
    val LATIN_SCRIPT_SYLHETI = booleanPreferencesKey("LATIN_SCRIPT_SYLHETI")
    val BENGALI_SCRIPT_BENGALI = booleanPreferencesKey("BENGALI_SCRIPT_BENGALI")
    val BENGALI_SCRIPT_SYLHETI = booleanPreferencesKey("BENGALI_SCRIPT_SYLHETI")

    val LANGUAGE = stringPreferencesKey("LANGUAGE")
    val DYNAMIC_THEME = booleanPreferencesKey("DYNAMIC_THEME")
}
