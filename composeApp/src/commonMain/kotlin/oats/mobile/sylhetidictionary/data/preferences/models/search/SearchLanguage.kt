package oats.mobile.sylhetidictionary.data.preferences.models.search

import androidx.datastore.preferences.core.Preferences
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti

sealed interface SearchLanguage: SettingEnum {

    val settingsKey: Preferences.Key<Boolean>
    override val label: StringResource

    companion object {
        val entries: List<SearchLanguage> =
            SearchLanguage.Latin.entries + SearchLanguage.EasternNagri.entries
    }

    enum class Latin(
        override val settingsKey: Preferences.Key<Boolean>,
        override val label: StringResource
    ) : SearchLanguage {

        ENGLISH(
            settingsKey = PreferenceKey.ENGLISH_SEARCH_LANGUAGE,
            label = Res.string.english
        ),

        SYLHETI(
            settingsKey = PreferenceKey.LATIN_SCRIPT_SYLHETI_SEARCH_LANGUAGE,
            label = Res.string.sylheti,
        )
    }

    enum class EasternNagri(
        override val settingsKey: Preferences.Key<Boolean>,
        override val label: StringResource
    ) : SearchLanguage {

        BENGALI(
            settingsKey = PreferenceKey.BENGALI_SEARCH_LANGUAGE,
            label = Res.string.bengali
        ),

        SYLHETI(
            settingsKey = PreferenceKey.EASTERN_NAGRI_SCRIPT_SYLHETI_SEARCH_LANGUAGE,
            label = Res.string.sylheti
        )
    }
}
