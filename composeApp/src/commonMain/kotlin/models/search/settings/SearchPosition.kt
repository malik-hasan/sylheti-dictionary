package models.search.settings

import androidx.datastore.preferences.core.Preferences
import data.settings.PreferenceKey

enum class SearchPosition(val settingsKey: Preferences.Key<Boolean>) {
    Beginning(PreferenceKey.SEARCH_BEGINNING),
    Middle(PreferenceKey.SEARCH_MIDDLE),
    End(PreferenceKey.SEARCH_END)
}
