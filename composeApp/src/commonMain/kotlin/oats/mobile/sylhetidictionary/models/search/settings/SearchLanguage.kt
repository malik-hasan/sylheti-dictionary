package oats.mobile.sylhetidictionary.models.search.settings

import androidx.datastore.preferences.core.Preferences
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryRepository
import oats.mobile.sylhetidictionary.data.settings.PreferenceKey
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti

sealed interface SearchLanguage: SettingEnum {

    val settingsKey: Preferences.Key<Boolean>
    override val label: StringResource

    suspend fun search(
        dictionaryRepository: DictionaryRepository,
        positionedQuery: String,
        simpleQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ): List<DictionaryEntry>

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
        ) {
            override suspend fun search(
                dictionaryRepository: DictionaryRepository,
                positionedQuery: String,
                simpleQuery: String,
                searchDefinitions: Boolean,
                searchExamples: Boolean
            ) = dictionaryRepository.searchEnglish(
                positionedQuery = positionedQuery,
                simpleQuery = simpleQuery,
                searchDefinitions = searchDefinitions,
                searchExamples = searchExamples
            )
        },

        SYLHETI(
            settingsKey = PreferenceKey.LATIN_SCRIPT_SYLHETI_SEARCH_LANGUAGE,
            label = Res.string.sylheti,
        ) {
            override suspend fun search(
                dictionaryRepository: DictionaryRepository,
                positionedQuery: String,
                simpleQuery: String,
                searchDefinitions: Boolean,
                searchExamples: Boolean
            ) = dictionaryRepository.searchSylhetiLatin(
                positionedQuery = positionedQuery,
                simpleQuery = simpleQuery,
                searchDefinitions = searchDefinitions,
                searchExamples = searchExamples
            )
        }
    }

    enum class EasternNagri(
        override val settingsKey: Preferences.Key<Boolean>,
        override val label: StringResource
    ) : SearchLanguage {

        BENGALI(
            settingsKey = PreferenceKey.BENGALI_SEARCH_LANGUAGE,
            label = Res.string.bengali
        ) {
            override suspend fun search(
                dictionaryRepository: DictionaryRepository,
                positionedQuery: String,
                simpleQuery: String,
                searchDefinitions: Boolean,
                searchExamples: Boolean
            ) = dictionaryRepository.searchBengali(
                simpleQuery = simpleQuery,
                searchDefinitions = searchDefinitions,
                searchExamples = searchExamples
            )
        },

        SYLHETI(
            settingsKey = PreferenceKey.EASTERN_NAGRI_SCRIPT_SYLHETI_SEARCH_LANGUAGE,
            label = Res.string.sylheti
        ) {
            override suspend fun search(
                dictionaryRepository: DictionaryRepository,
                positionedQuery: String,
                simpleQuery: String,
                searchDefinitions: Boolean,
                searchExamples: Boolean
            ) = dictionaryRepository.searchSylhetiBengali(
                positionedQuery = positionedQuery,
                simpleQuery = simpleQuery,
                searchExamples = searchExamples
            )
        }
    }
}
