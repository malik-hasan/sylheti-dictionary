package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import ui.theme.getBodyFontFamily
import ui.utils.appendHighlighted

data class FieldTag(
    val label: String,
    val fontFamily: FontFamily? = null
)

data class FieldBody(
    val body: String,
    val fontFamily: FontFamily? = null
)

@Composable
fun TaggedField(
    tag: FieldTag,
    body: FieldBody,
    highlightRegex: Regex
) { TaggedField(tag, listOf(body), highlightRegex) }

@Composable
fun TaggedField(
    tag: FieldTag,
    bodies: List<FieldBody>,
    highlightRegex: Regex
) {
    val overflowIndent = 40.sp

    Text(
        text = buildAnnotatedString {
            pushStyle(ParagraphStyle(textIndent = TextIndent(restLine = overflowIndent)))
            with(tag) {
                withStyle(
                    SpanStyle(
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        fontFamily = fontFamily ?: getBodyFontFamily(),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                ) { append(label.lowercase()) }
            }
            append(' ')
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