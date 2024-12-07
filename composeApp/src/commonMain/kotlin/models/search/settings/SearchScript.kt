package models.search.settings

import oats.mobile.sylhetidictionary.DictionaryEntry
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.auto
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.latin_ipa
import sylhetidictionary.composeapp.generated.resources.nagri

enum class SearchScript(
    override val label: StringResource,
    val sortAlgorithm: (DictionaryEntry) -> String?,
    val regexCharSet: Regex? = null,
    val languages: List<SearchLanguage> = emptyList()
): SettingEnum {

    AUTO(
        label = Res.string.auto,
        sortAlgorithm = { it.citationIPA ?: it.lexemeIPA }
    ),

    LATIN(
        label = Res.string.latin_ipa,
        sortAlgorithm = { it.citationIPA ?: it.lexemeIPA },
        regexCharSet = Regex("\\p{IsLatin}"),
        languages = SearchLanguage.Latin.entries
    ),

    BENGALI(
        label = Res.string.bengali,
        sortAlgorithm = { it.citationBengali ?: it.lexemeBengali },
        regexCharSet = Regex("\\p{IsBengali}"),
        languages = SearchLanguage.Bengali.entries
    ),

    NAGRI(
        label = Res.string.nagri,
        sortAlgorithm = { it.citationNagri ?: it.lexemeNagri },
        regexCharSet = Regex("\\p{IsSyloti_Nagri}")
    );

    val isLatinOrAuto: Boolean
        get() = this in setOf(LATIN, AUTO)
}
