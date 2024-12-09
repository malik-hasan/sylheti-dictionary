package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import ui.theme.getBodyFontFamily
import ui.utils.StringWithFont
import ui.utils.appendHighlighted

@Composable
fun TaggedField(
    tag: StringWithFont,
    body: StringWithFont,
    highlightRegex: Regex
) { TaggedField(tag, listOf(body), highlightRegex) }

@Composable
fun TaggedField(
    tag: StringWithFont,
    bodies: List<StringWithFont>,
    highlightRegex: Regex
) {
    val overflowIndent = 40.sp

    Text(
        text = buildAnnotatedString {
            pushStyle(ParagraphStyle(textIndent = TextIndent(restLine = overflowIndent)))
            with(tag) {
                if (string.isNotBlank()) {
                    withStyle(
                        MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) { append(string.lowercase()) }
                    append(' ')
                }
            }
            bodies.forEachIndexed { i, (body, fontFamily) ->
                if (i > 0) {
                    pop()
                    pushStyle(ParagraphStyle(textIndent = TextIndent(overflowIndent, overflowIndent)))
                }

                withStyle(SpanStyle(fontFamily = fontFamily ?: getBodyFontFamily())) {
                    appendHighlighted(body, highlightRegex)
                }
            }
        },
        style = MaterialTheme.typography.bodyMedium
    )
}
