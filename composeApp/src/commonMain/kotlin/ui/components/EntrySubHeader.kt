package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import ui.screens.search.LocalHighlightRegex
import ui.theme.latinBodyFontFamily
import ui.theme.latinDisplayFontFamily
import ui.utils.appendHighlighted

@Composable
fun EntrySubHeader(
    partOfSpeech: String?,
    partOfSpeechStyle: TextStyle,
    gloss: String?,
    glossStyle: TextStyle,
    modifier: Modifier = Modifier,
    highlightRegex: Regex = LocalHighlightRegex.current
) {
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
                    .padding(horizontal = 3.dp)
                    .then(modifier),
                style = partOfSpeechStyle,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold,
                fontFamily = latinDisplayFontFamily
            )
        }

        gloss?.let {
            Text(
                text = buildAnnotatedString {
                    appendHighlighted(it, highlightRegex)
                },
                style = glossStyle,
                fontFamily = latinBodyFontFamily
            )
        }
    }
}
