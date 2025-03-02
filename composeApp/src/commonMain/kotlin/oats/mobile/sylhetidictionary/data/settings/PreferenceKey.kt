package oats.mobile.sylhetidictionary.data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKey {
    val CURRENT_DICTIONARY_VERSION = intPreferencesKey("CURRENT_DICTIONARY_VERSION")

    val SEARCH_POSITION = intPreferencesKey("SEARCH_POSITION")
    val SEARCH_SCRIPT = intPreferencesKey("SEARCH_SCRIPT")
    val ENGLISH_SEARCH_LANGUAGE = booleanPreferencesKey("ENGLISH_SEARCH_LANGUAGE")
    val LATIN_SCRIPT_SYLHETI_SEARCH_LANGUAGE = booleanPreferencesKey("LATIN_SCRIPT_SYLHETI_SEARCH_LANGUAGE")
    val BENGALI_SEARCH_LANGUAGE = booleanPreferencesKey("BENGALI_SEARCH_LANGUAGE")
    val EASTERN_NAGRI_SCRIPT_SYLHETI_SEARCH_LANGUAGE = booleanPreferencesKey("EASTERN_NAGRI_SCRIPT_SYLHETI_SEARCH_LANGUAGE")
    val SEARCH_DEFINITIONS = booleanPreferencesKey("SEARCH_DEFINITIONS")
    val SEARCH_EXAMPLES = booleanPreferencesKey("SEARCH_EXAMPLES")

    val HIGHLIGHT_REGEX = stringPreferencesKey("HIGHLIGHT_REGEX")

    val LANGUAGE = stringPreferencesKey("LANGUAGE")
    val THEME = intPreferencesKey("THEME")
    val DYNAMIC_THEME = booleanPreferencesKey("DYNAMIC_THEME")
}
