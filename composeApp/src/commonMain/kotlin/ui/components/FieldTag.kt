package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle

@Composable
fun FieldTag(tag: String, tagFontFamily: FontFamily?, modifier: Modifier = Modifier) {
    Text(
        text = tag.lowercase(),
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontFamily = tagFontFamily
    )
}
