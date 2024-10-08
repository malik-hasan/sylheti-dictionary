package models.search.settings

import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.auto
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.latin_ipa
import sylhetidictionary.composeapp.generated.resources.nagri

enum class SearchScript(
    val label: StringResource,
    val regexCharSet: Regex? = null,
    val languages: List<SearchLanguage> = emptyList()
) {
    AUTO(Res.string.auto),
    LATIN(Res.string.latin_ipa, Regex("\\p{Latin}"), SearchLanguage.Latin.entries),
    BENGALI(Res.string.bengali, Regex("\\p{Bengali}"), SearchLanguage.Bengali.entries),
    NAGRI(Res.string.nagri, Regex("\\p{Syloti_Nagri}");
}
