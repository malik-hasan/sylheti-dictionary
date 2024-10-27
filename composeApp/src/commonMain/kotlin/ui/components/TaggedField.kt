package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import ui.theme.englishBodyFontFamily
import ui.utils.appendHighlighted

@Composable
fun TaggedField(tag: String, body: CharSequence, highlightRegex: Regex) {
    Text(
        text = buildAnnotatedString {
            pushStyle(ParagraphStyle(textIndent = TextIndent(restLine = 30.sp)))
            withStyle(
                SpanStyle(
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                    fontFamily = englishBodyFontFamily,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            ) { append(tag) }
            append(' ')
            appendHighlighted(body, highlightRegex)
            pop()
        },
        style = MaterialTheme.typography.bodyMedium
    )
}