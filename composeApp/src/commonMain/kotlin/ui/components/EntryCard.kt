package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import models.displayBengali
import models.displayIPA
import models.displayNagri
import oats.mobile.sylhetidictionary.DictionaryEntry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.bookmark
import sylhetidictionary.composeapp.generated.resources.bookmark_border
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti
import ui.app.LocalShowNagri
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.utils.StringWithFont
import ui.utils.appendHighlighted

@Composable
fun EntryCard(
    entry: DictionaryEntry,
    isBookmark: Boolean,
    onBookmark: () -> Unit,
    modifier: Modifier = Modifier,
    showNagri: Boolean = LocalShowNagri.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current,
    additionalContent: @Composable ColumnScope.() -> Unit = {}
) {
    Card(modifier) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = buildAnnotatedString {
                        with(entry) {
                            withStyle(SpanStyle(fontFamily = latinBodyFontFamily)) {
                                appendHighlighted(displayIPA, mappedIpaHighlightRegex)
                            }
                            displayBengali?.let {
                                append(" • ")
                                withStyle(SpanStyle(fontFamily = bengaliBodyFontFamily)) {
                                    appendHighlighted(it, highlightRegex)
                                }
                            }
                            if (showNagri) {
                                displayNagri?.let {
                                    append(" • ")
                                    appendHighlighted(it, highlightRegex)
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Icon(
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = ripple(bounded = false, radius = 20.dp),
                        onClick = onBookmark
                    ),
                    painter = painterResource(
                        if (isBookmark) {
                            Res.drawable.bookmark
                        } else Res.drawable.bookmark_border
                    ),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "bookmark"
                )
            }

            with(entry) {
                TaggedField(
                    tag = StringWithFont(partOfSpeech ?: "pos?", latinBodyFontFamily),
                    body = StringWithFont(gloss ?: "", latinBodyFontFamily),
                    highlightRegex = highlightRegex
                )

                definitionEN?.let { definition ->
                    TaggedField(
                        tag = StringWithFont(stringResource(Res.string.english)),
                        body = StringWithFont(definition, latinBodyFontFamily),
                        highlightRegex = highlightRegex
                    )
                }

                listOfNotNull(
                    definitionBN,
                    definitionBNIPA
                ).takeIf { it.isNotEmpty() }?.let {
                    TaggedField(
                        tag = StringWithFont(stringResource(Res.string.bengali)),
                        bodies = listOfNotNull(
                            definitionBN?.let { StringWithFont(it, bengaliBodyFontFamily) },
                            definitionBNIPA?.let { StringWithFont(it, latinBodyFontFamily) }
                        ),
                        highlightRegex = mappedIpaHighlightRegex
                    )
                }

                listOfNotNull(
                    definitionNagri.takeIf { showNagri },
                    definitionIPA
                ).takeIf { it.isNotEmpty() }?.let { definitions ->
                    TaggedField(
                        tag = StringWithFont(stringResource(Res.string.sylheti)),
                        bodies = definitions.map { StringWithFont(it, latinBodyFontFamily) },
                        highlightRegex = mappedIpaHighlightRegex
                    )
                }
            }

            additionalContent()
        }
    }
}
