package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.runtime.Composable
import oats.mobile.sylhetidictionary.Variant
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchScript
import oats.mobile.sylhetidictionary.ui.screens.search.LocalHighlightRegex
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily
import oats.mobile.sylhetidictionary.ui.models.SDString

@Composable
fun EntryVariant(
    variant: Variant,
    highlightRegex: Regex = LocalHighlightRegex.current,
) = TaggedField(
    tag = buildString {
        append('-')
        variant.environment?.let {
            append(" ${it.lowercase()}")
        }
    },
    tagFontFamily = latinDisplayFontFamily,
    bodies = listOfNotNull(
        SDString(variant.variantIPA, highlightRegex, SearchScript.LATIN),
        variant.variantEN?.let { SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI) },
        variant.variantSN?.let { SDString(it, highlightRegex) }
    ),
    separator = " • "
)
