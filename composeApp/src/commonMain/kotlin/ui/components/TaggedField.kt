package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import ui.utils.SDString
import ui.utils.appendHighlighted

@Composable
fun TaggedField(
    tag: String,
    body: SDString,
    modifier: Modifier = Modifier,
    enableSelection: Boolean = true,
    separator: String = "\n",
    bodyStyle: TextStyle = MaterialTheme.typography.bodyMedium
) { TaggedField(tag, listOf(body), modifier, enableSelection, separator, bodyStyle) }

@Composable
fun TaggedField(
    tag: String,
    bodies: List<SDString>,
    modifier: Modifier = Modifier,
    enableSelection: Boolean = true,
    separator: String = "\n",
    bodyStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = tag.lowercase(),
            modifier = Modifier.padding(top = 3.dp),
            style = MaterialTheme.typography.labelMedium,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        @Composable
        fun BodyText() = Text(
            text = buildAnnotatedString {
                bodies.forEachIndexed { i, body ->
                    if (i > 0) append(separator)
                    appendHighlighted(body)
                }
            },
            style = bodyStyle
        )

        if (enableSelection) {
            SelectionContainer {
                BodyText()
            }
        } else BodyText()
    }
}
