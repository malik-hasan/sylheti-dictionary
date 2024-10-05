package models.search.settings

import androidx.datastore.preferences.core.Preferences
import data.settings.PreferenceKey
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti

sealed interface SearchLanguage {

    val settingsKey: Preferences.Key<Boolean>
    val label: StringResource

    companion object {
        val entries: List<SearchLanguage> = SearchLanguage.Latin.entries + SearchLanguage.Bengali.entries
    }

    enum class Latin(override val settingsKey: Preferences.Key<Boolean>, override val label: StringResource): SearchLanguage {
        ENGLISH(PreferenceKey.LATIN_SCRIPT_ENGLISH, Res.string.english),
        SYLHETI(PreferenceKey.LATIN_SCRIPT_SYLHETI, Res.string.sylheti);
    }

    enum class Bengali(override val settingsKey: Preferences.Key<Boolean>, override val label: StringResource): SearchLanguage {
        BENGALI(PreferenceKey.BENGALI_SCRIPT_BENGALI, Res.string.bengali),
        SYLHETI(PreferenceKey.BENGALI_SCRIPT_SYLHETI, Res.string.sylheti);
    }
}
