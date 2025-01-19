package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import ui.utils.SDString
import ui.utils.appendHighlighted

@Composable
fun TaggedField(
    tag: String,
    body: SDString,
    modifier: Modifier = Modifier,
    tagFontFamily: FontFamily? = null,
    enableSelection: Boolean = true,
    separator: String = "\n",
) { TaggedField(tag, listOf(body), modifier, tagFontFamily, enableSelection, separator) }

@Composable
fun TaggedField(
    tag: String,
    bodies: List<SDString>,
    modifier: Modifier = Modifier,
    tagFontFamily: FontFamily? = null,
    enableSelection: Boolean = true,
    separator: String = "\n"
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FieldTag(tag, tagFontFamily, Modifier.padding(top = 4.dp))

        @Composable
        fun BodyText() = Text(
            text = buildAnnotatedString {
                bodies.forEachIndexed { i, body ->
                    if (i > 0) append(separator)
                    appendHighlighted(body)
                }
            }
        )

        if (enableSelection) {
            SelectionContainer {
                BodyText()
            }
        } else BodyText()
    }
}
