package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.Variant
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.ui.screens.search.LocalHighlightRegex
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily
import oats.mobile.sylhetidictionary.ui.utils.SDString

@Composable
fun EntryVariant(
    variant: Variant,
    index: Int,
    showIndex: Boolean,
    highlightRegex: Regex = LocalHighlightRegex.current,
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showIndex) Text("${index + 1}.")

        TaggedField(
            tag = variant.environment?.lowercase().takeIf { it != "unspecified variant" } ?: "variant",
            tagFontFamily = latinDisplayFontFamily,
            bodies = listOfNotNull(
                SDString(variant.variantIPA, highlightRegex, SearchScript.LATIN),
                variant.variantBengali?.let { SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI) },
                variant.variantNagri?.let { SDString(it, highlightRegex) }
            ),
            separator = " • "
        )
    }
}
