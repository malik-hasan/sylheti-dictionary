package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.DictionaryEntry
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bookmark
import sylhetidictionary.composeapp.generated.resources.bookmark_border
import ui.theme.bengaliBodyFontFamily
import ui.utils.appendHighlighted

@Composable
fun EntryCard(
    entry: DictionaryEntry,
    highlightRegex: Regex,
    bookmarksRepository: BookmarksRepository = koinInject(),
    onBookmark: (entryId: String, isBookmark: Boolean) -> Unit
) {

    var isBookmark by remember { mutableStateOf(false) }
    LaunchedEffect(entry.entryId) {
        isBookmark = bookmarksRepository.checkBookmark(entry.entryId)
    }

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        appendHighlighted(entry.citationIPA ?: entry.lexemeIPA, highlightRegex)
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
                        onClick = {
                            isBookmark = !isBookmark
                            onBookmark(entry.entryId, isBookmark)
                        }
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
                    highlightRegex = highlightRegex
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
                    highlightRegex = highlightRegex
                )
            }
        }
    }
}
