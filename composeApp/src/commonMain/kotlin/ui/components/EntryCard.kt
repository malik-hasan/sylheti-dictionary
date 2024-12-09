package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

@Composable
fun EntryCard(
    entry: DictionaryEntry,
    isBookmark: Boolean,
    modifier: Modifier = Modifier,
    showNagri: Boolean = LocalShowNagri.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current,
    onBookmark: () -> Unit
) {
    Card(modifier) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                with(entry) {
                    EntryHeader(
                        modifier = Modifier.weight(1f),
                        displayIPA = citationIPA ?: lexemeIPA,
                        displayBengali = citationBengali ?: lexemeBengali,
                        displayNagri = citationNagri ?: lexemeNagri
                    )
                }

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
                EntrySubHeader(
                    partOfSpeech = partOfSpeech,
                    partOfSpeechStyle = MaterialTheme.typography.labelMedium,
                    gloss = gloss,
                    glossStyle = MaterialTheme.typography.bodyLarge
                )

                listOfNotNull(
                    definitionEN,
                    definitionBN,
                    definitionBNIPA,
                    definitionNagri.takeIf { showNagri },
                    definitionIPA
                ).takeIf { it.isNotEmpty() }?.let {

                    HorizontalDivider(Modifier
                        .padding(horizontal = 24.dp, vertical = 4.dp)
                        .widthIn(max = 500.dp)
                        .align(Alignment.CenterHorizontally)
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
            }
        }
    }
}
