package models.search.settings

import androidx.datastore.preferences.core.Preferences
import data.settings.SettingsKey

enum class SearchPosition(val settingsKey: Preferences.Key<Boolean>) {
    Beginning(SettingsKey.SEARCH_BEGINNING),
    Middle(SettingsKey.SEARCH_MIDDLE),
    End(SettingsKey.SEARCH_END)
}
