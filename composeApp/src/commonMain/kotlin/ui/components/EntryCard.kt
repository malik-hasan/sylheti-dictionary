package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
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
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bookmark
import sylhetidictionary.composeapp.generated.resources.bookmark_border
import ui.screens.search.ExtendedEntryData
import ui.screens.search.SearchEvent
import ui.theme.bengaliBodyFontFamily
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
            .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        appendHighlighted(entry.citationIPA ?: entry.lexemeIPA, mappedIpaHighlightRegex)
                        entry.citationBengali ?: entry.lexemeBengali?.let {
                            append(" • ")
                            withStyle(SpanStyle(fontFamily = bengaliBodyFontFamily)) {
                                appendHighlighted(it, highlightRegex)
                            }
                        }
                        entry.citationNagri ?: entry.lexemeNagri?.let {
                            append(" • ")
                            appendHighlighted(it, highlightRegex)
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

            TaggedField(
                tag = entry.partOfSpeech?.lowercase() ?: "unknown",
                body = entry.gloss ?: "",
                highlightRegex = highlightRegex
            )

            entry.definitionEN?.let { def ->
                TaggedField("english", def, highlightRegex)
            }

            if (entry.definitionBN != null || entry.definitionBNIPA != null) {
                TaggedField(
                    tag = "bengali",
                    body = buildAnnotatedString {
                        entry.definitionBN?.let {
                            withStyle(SpanStyle(fontFamily = bengaliBodyFontFamily)) {
                                append("$it ")
                            }
                        }
                        entry.definitionBNIPA?.let {
                            append(it)
                        }
                    },
                    highlightRegex = mappedIpaHighlightRegex
                )
            }

            if (entry.definitionNagri != null || entry.definitionIPA != null) {
                TaggedField(
                    tag = "sylheti",
                    body = buildAnnotatedString {
                        entry.definitionNagri?.let {
                            append("$it ")
                        }
                        entry.definitionIPA?.let {
                            append(it)
                        }
                    },
                    highlightRegex = mappedIpaHighlightRegex
                )
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
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            HorizontalDivider()
            extendedEntry.examples.forEach {
                it.exampleIPA?.let { it1 -> Text(it1) }
            }
        }
    }
}
