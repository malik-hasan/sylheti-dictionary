package oats.mobile.sylhetidictionary.models.search.settings

import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.auto
import sylhetidictionary.composeapp.generated.resources.eastern_nagri
import sylhetidictionary.composeapp.generated.resources.latin_ipa
import sylhetidictionary.composeapp.generated.resources.sylheti_nagri

enum class SearchScript(
    override val label: StringResource,
    val regexCharSet: Regex? = null,
    val languages: List<SearchLanguage> = emptyList()
): SettingEnum {

    AUTO(Res.string.auto),

    LATIN(
        label = Res.string.latin_ipa,
        regexCharSet = Regex("\\p{IsLatin}"),
        languages = SearchLanguage.Latin.entries
    ),

    EASTERN_NAGRI(
        label = Res.string.eastern_nagri,
        regexCharSet = Regex("\\p{IsBengali}"),
        languages = SearchLanguage.EasternNagri.entries
    ),

    SYLHETI_NAGRI(
        label = Res.string.sylheti_nagri,
        regexCharSet = Regex("\\p{IsSyloti_Nagri}")
    );
}
