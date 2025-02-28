package oats.mobile.sylhetidictionary.models.search.settings

import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.anywhere
import sylhetidictionary.composeapp.generated.resources.end
import sylhetidictionary.composeapp.generated.resources.full_match
import sylhetidictionary.composeapp.generated.resources.start

enum class SearchPosition(override val label: StringResource): SettingEnum {
    ANYWHERE(Res.string.anywhere) {
        override fun getPositionedQuery(term: String) = "*$term*"
        override fun getSuggestionQuery(term: String) = getPositionedQuery(term)
    },
    START(Res.string.start) {
        override fun getPositionedQuery(term: String) = "$term*"
        override fun getSuggestionQuery(term: String) = getPositionedQuery(term)
    },
    END(Res.string.end) {
        override fun getPositionedQuery(term: String) = "*$term"
        override fun getSuggestionQuery(term: String) = ANYWHERE.getPositionedQuery(term)
    },
    FULL_MATCH(Res.string.full_match) {
        override fun getPositionedQuery(term: String) = term
        override fun getSuggestionQuery(term: String) = START.getPositionedQuery(term)
    };

    abstract fun getPositionedQuery(term: String): String
    abstract fun getSuggestionQuery(term: String): String
}
