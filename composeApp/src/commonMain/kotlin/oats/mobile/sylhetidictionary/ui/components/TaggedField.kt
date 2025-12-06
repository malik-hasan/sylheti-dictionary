package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.ui.models.SDString
import oats.mobile.sylhetidictionary.ui.utils.appendHighlighted

@Composable
fun TaggedField(
    tag: String,
    body: SDString,
    modifier: Modifier = Modifier,
    tagFontFamily: FontFamily? = null,
    separator: String = "\n",
) = TaggedField(tag, listOf(body), modifier, tagFontFamily, separator)

@Composable
fun TaggedField(
    tag: String,
    bodies: List<SDString>,
    modifier: Modifier = Modifier,
    tagFontFamily: FontFamily? = null,
    separator: String = "\n"
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DisableSelection {
            FieldTag(tag, tagFontFamily, Modifier.padding(top = 3.dp))
        }

        Text(
            text = buildAnnotatedString {
                bodies.forEachIndexed { i, body ->
                    if (i > 0) append(separator)
                    appendHighlighted(body)
                }
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
