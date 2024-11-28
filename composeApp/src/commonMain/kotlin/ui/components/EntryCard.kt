package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.DictionaryEntry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.bookmark
import sylhetidictionary.composeapp.generated.resources.bookmark_border
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.example
import sylhetidictionary.composeapp.generated.resources.sylheti
import ui.screens.search.ExtendedEntryData
import ui.screens.search.SearchEvent
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.utils.StringWithFont
import ui.utils.appendHighlighted

@Composable
fun EntryCard(
    entry: DictionaryEntry,
    extendedEntry: ExtendedEntryData,
    highlightRegex: Regex,
    mappedIpaHighlightRegex: Regex,
    onEvent: (SearchEvent) -> Unit
) {
    val isBookmark = extendedEntry.isBookmark

    val expanded = extendedEntry.isExpanded
    val expandTransition = updateTransition(expanded)
    val expandIconRotation by expandTransition.animateFloat { state ->
        if (state) 180f else 0f
    }

    Card {
        Column(Modifier
            .padding(top = 12.dp)
            .padding(horizontal = 16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = buildAnnotatedString {
                        with(entry) {
                            withStyle(SpanStyle(fontFamily = latinBodyFontFamily)) {
                                appendHighlighted(citationIPA ?: lexemeIPA, mappedIpaHighlightRegex)
                            }
                            citationBengali ?: lexemeBengali?.let {
                                append(" • ")
                                withStyle(SpanStyle(fontFamily = bengaliBodyFontFamily)) {
                                    appendHighlighted(it, highlightRegex)
                                }
                            }
                            citationNagri ?: lexemeNagri?.let {
                                append(" • ")
                                appendHighlighted(it, highlightRegex)
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
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false, radius = 20.dp),
                        onClick = { onEvent(SearchEvent.Bookmark(entry.entryId, !isBookmark)) }
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
                    definitionNagri,
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

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Expand",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(SearchEvent.ExpandItem(entry, !expanded)) }
                .rotate(expandIconRotation)
        )

        expandTransition.AnimatedVisibility(
            visible = { it },
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            HorizontalDivider()
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                extendedEntry.examples.forEachIndexed { i, example ->
                    Column {
                        Text(stringResource(Res.string.example, i + 1))

                        with(example) {
                            listOfNotNull(
                                exampleBengali,
                                exampleNagri,
                                exampleIPA
                            ).takeIf { it.isNotEmpty() }?.let {
                                TaggedField(
                                    tag = StringWithFont(stringResource(Res.string.sylheti)),
                                    bodies = listOfNotNull(
                                        exampleBengali?.let { StringWithFont(it, bengaliBodyFontFamily) },
                                        exampleNagri?.let(::StringWithFont),
                                        exampleIPA?.let { StringWithFont(it, latinBodyFontFamily) }
                                    ),
                                    highlightRegex = mappedIpaHighlightRegex
                                )
                            }

                            exampleEN?.let { example ->
                                TaggedField(
                                    tag = StringWithFont(stringResource(Res.string.english)),
                                    body = StringWithFont(example, latinBodyFontFamily),
                                    highlightRegex = highlightRegex
                                )
                            }

                            listOfNotNull(
                                exampleBN,
                                exampleBNIPA
                            ).takeIf { it.isNotEmpty() }?.let {
                                TaggedField(
                                    tag = StringWithFont(stringResource(Res.string.bengali)),
                                    bodies = listOfNotNull(
                                        exampleBN?.let { StringWithFont(it, bengaliBodyFontFamily) },
                                        exampleBNIPA?.let { StringWithFont(it, latinBodyFontFamily) }
                                    ),
                                    highlightRegex = mappedIpaHighlightRegex
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
