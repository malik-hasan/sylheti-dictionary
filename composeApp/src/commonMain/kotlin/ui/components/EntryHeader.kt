package ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.screens.search.LocalSharedTransitionScope
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.utils.appendHighlighted
import ui.utils.ifTrue

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EntryHeader(
    entryId: String,
    displayIPA: String,
    displayBengali: String?,
    displayNagri: String?,
    displayStyle: TextStyle,
    partOfSpeech: String?,
    partOfSpeechStyle: TextStyle,
    gloss: String?,
    glossStyle: TextStyle,
    modifier: Modifier = Modifier,
    includeAnimation: Boolean = true,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current
) {
    with(sharedTransitionScope) {
        Column(modifier) {
            SelectionContainer {
                Text(
                    text = buildAnnotatedString {
                        appendHighlighted(displayIPA, mappedIpaHighlightRegex, latinBodyFontFamily)

                        displayBengali?.let {
                            append(" • ")
                            appendHighlighted(it, highlightRegex, bengaliBodyFontFamily)
                        }

                        displayNagri?.let {
                            append(" • ")
                            appendHighlighted(it, highlightRegex)
                        }
                    },
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    style = displayStyle,
                    modifier = Modifier.ifTrue(includeAnimation) {
                        sharedBounds(
                            sharedContentState = rememberSharedContentState("display-$entryId"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                partOfSpeech?.let {
                    Chip(
                        text = it.lowercase(),
                        style = partOfSpeechStyle,
                        modifier = Modifier.ifTrue(includeAnimation) {
                            sharedBounds(
                                sharedContentState = rememberSharedContentState("part-of-speech-$entryId"),
                                animatedVisibilityScope = animatedContentScope
                            )
                        }
                    )
                }

                gloss?.let {
                    SelectionContainer {
                        Text(
                            text = buildAnnotatedString {
                                appendHighlighted(it, highlightRegex, latinBodyFontFamily)
                            },
                            style = glossStyle,
                            modifier = Modifier.ifTrue(includeAnimation) {
                                sharedBounds(
                                    sharedContentState = rememberSharedContentState("gloss-$entryId"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
