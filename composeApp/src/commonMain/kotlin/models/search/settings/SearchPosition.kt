package models.search.settings

import androidx.datastore.preferences.core.Preferences
import data.settings.PreferenceKey
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.beginning
import sylhetidictionary.composeapp.generated.resources.end
import sylhetidictionary.composeapp.generated.resources.full_match
import sylhetidictionary.composeapp.generated.resources.middle

enum class SearchPosition(val settingsKey: Preferences.Key<Boolean>, val label: StringResource) {
    FULL_MATCH(PreferenceKey.SEARCH_FULL_MATCH, Res.string.full_match),
    BEGINNING(PreferenceKey.SEARCH_BEGINNING, Res.string.beginning),
    MIDDLE(PreferenceKey.SEARCH_MIDDLE, Res.string.middle),
    END(PreferenceKey.SEARCH_END, Res.string.end);
}
