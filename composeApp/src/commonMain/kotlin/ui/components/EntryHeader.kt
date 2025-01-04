package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.app.LocalShowNagri
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.theme.latinDisplayFontFamily
import ui.utils.appendHighlighted

@Composable
fun EntryHeader(
    displayIPA: String,
    displayBengali: String?,
    displayNagri: String?,
    displayStyle: TextStyle,
    partOfSpeech: String?,
    partOfSpeechStyle: TextStyle,
    gloss: String?,
    glossStyle: TextStyle,
    modifier: Modifier = Modifier,
    showNagri: Boolean = LocalShowNagri.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current,
) {
    Column(modifier) {
        Text(
            text = buildAnnotatedString {
                displayBengali?.let {
                    appendHighlighted(it, highlightRegex, bengaliBodyFontFamily)
                    append(" • ")
                }

                appendHighlighted(displayIPA, mappedIpaHighlightRegex, latinBodyFontFamily)

                if (showNagri) {
                    displayNagri?.let {
                        append(" • ")
                        appendHighlighted(it, highlightRegex)
                    }
                }
            },
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            style = displayStyle
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            partOfSpeech?.let {
                Text(
                    text = it.lowercase(),
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 3.dp),
                    style = partOfSpeechStyle,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = latinDisplayFontFamily
                )
            }

            gloss?.let {
                Text(
                    text = buildAnnotatedString {
                        appendHighlighted(it, highlightRegex, latinBodyFontFamily)
                    },
                    style = glossStyle
                )
            }
        }
    }
}
