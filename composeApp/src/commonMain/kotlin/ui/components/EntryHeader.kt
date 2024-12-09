package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import ui.app.LocalShowNagri
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.utils.appendHighlighted

@Composable
fun EntryHeader(
    displayIPA: String,
    displayBengali: String?,
    displayNagri: String?,
    modifier: Modifier = Modifier,
    showNagri: Boolean = LocalShowNagri.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontFamily = latinBodyFontFamily)) {
                appendHighlighted(displayIPA, mappedIpaHighlightRegex)
            }
            displayBengali?.let {
                append(" • ")
                withStyle(SpanStyle(fontFamily = bengaliBodyFontFamily)) {
                    appendHighlighted(it, highlightRegex)
                }
            }
            if (showNagri) {
                displayNagri?.let {
                    append(" • ")
                    appendHighlighted(it, highlightRegex)
                }
            }
        },
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.primary
    )
}
