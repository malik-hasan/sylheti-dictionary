package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import ui.utils.SDString
import ui.utils.appendHighlighted

@Composable
fun TaggedField(
    tag: String,
    body: SDString
) { TaggedField(tag, listOf(body)) }

@Composable
fun TaggedField(
    tag: String,
    bodies: List<SDString>
) {
    val overflowIndent = 40.sp

    Text(
        text = buildAnnotatedString {
            pushStyle(ParagraphStyle(textIndent = TextIndent(restLine = overflowIndent)))
            withStyle(
                MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) { append(tag.lowercase()) }
            append(' ')

            bodies.forEachIndexed { i, dictionaryString ->
                if (i > 0) {
                    pop()
                    pushStyle(ParagraphStyle(textIndent = TextIndent(overflowIndent, overflowIndent)))
                }

                appendHighlighted(dictionaryString)
            }
        },
        style = MaterialTheme.typography.bodyMedium
    )
}
