package models.search.settings

import androidx.datastore.preferences.core.Preferences
import data.dictionary.DictionaryDataSource
import data.settings.PreferenceKey
import oats.mobile.sylhetidictionary.DictionaryEntry
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti

sealed interface SearchLanguage: SettingEnum {

    val settingsKey: Preferences.Key<Boolean>
    override val label: StringResource

    val search: suspend DictionaryDataSource.(
        simpleQuery: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) -> List<DictionaryEntry>

    companion object {
        val entries: List<SearchLanguage> =
            SearchLanguage.Latin.entries + SearchLanguage.EasternNagri.entries
    }

    enum class Latin(
        override val settingsKey: Preferences.Key<Boolean>,
        override val label: StringResource,
        override val search: suspend DictionaryDataSource.(String, String, Boolean, Boolean) -> List<DictionaryEntry>
    ) : SearchLanguage {

        ENGLISH(
            settingsKey = PreferenceKey.ENGLISH_SEARCH_LANGUAGE,
            label = Res.string.english,
            search = DictionaryDataSource::searchEnglish
        ),

        SYLHETI(
            settingsKey = PreferenceKey.LATIN_SCRIPT_SYLHETI_SEARCH_LANGUAGE,
            label = Res.string.sylheti,
            search = DictionaryDataSource::searchSylhetiLatin
        );
    }

    enum class EasternNagri(
        override val settingsKey: Preferences.Key<Boolean>,
        override val label: StringResource,
        override val search: suspend DictionaryDataSource.(String, String, Boolean, Boolean) -> List<DictionaryEntry>
    ) : SearchLanguage {

        BENGALI(
            settingsKey = PreferenceKey.BENGALI_SEARCH_LANGUAGE,
            label = Res.string.bengali,
            search = { query, _, searchDefinitions, searchExamples ->
                searchBengali(query, searchDefinitions, searchExamples)
            }
        ),

        SYLHETI(
            settingsKey = PreferenceKey.EASTERN_NAGRI_SCRIPT_SYLHETI_SEARCH_LANGUAGE,
            label = Res.string.sylheti,
            search = { query, positionedQuery, _, searchExamples ->
                searchSylhetiBengali(query, positionedQuery, searchExamples)
            }
        );
    }
}
