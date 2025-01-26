package oats.mobile.sylhetidictionary.models.search.settings

import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.anywhere
import sylhetidictionary.composeapp.generated.resources.end
import sylhetidictionary.composeapp.generated.resources.full_match
import sylhetidictionary.composeapp.generated.resources.start

enum class SearchPosition(override val label: StringResource): SettingEnum {
    ANYWHERE(Res.string.anywhere) {
        override fun getQuery(term: String) = "*$term*"
    },
    START(Res.string.start) {
        override fun getQuery(term: String) = "$term*"
    },
    END(Res.string.end) {
        override fun getQuery(term: String) = "*$term"
    },
    FULL_MATCH(Res.string.full_match) {
        override fun getQuery(term: String) = term
    };

    abstract fun getQuery(term: String): String
}
