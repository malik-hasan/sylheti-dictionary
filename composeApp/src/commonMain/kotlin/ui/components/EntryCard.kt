package ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.displayBengali
import models.displayIPA
import models.displayNagri
import oats.mobile.sylhetidictionary.DictionaryEntry
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti
import ui.app.LocalShowNagri
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.screens.search.LocalSharedTransitionScope
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.utils.StringWithFont

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EntryCard(
    entry: DictionaryEntry,
    isBookmark: Boolean,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current,
    showNagri: Boolean = LocalShowNagri.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current,
    onBookmark: () -> Unit
) {
    with(sharedTransitionScope) {
        with(entry) {
            Card(
                modifier = modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState("container-$entryId"),
                    animatedVisibilityScope = animatedContentScope
                )
            ) {
                Column(Modifier.padding(bottom = 12.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        EntryHeader(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp, top = 8.dp)
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("header-$entryId"),
                                    animatedVisibilityScope = animatedContentScope
                                ),
                            displayIPA = displayIPA,
                            displayBengali = displayBengali,
                            displayNagri = displayNagri,
                            displayStyle = MaterialTheme.typography.bodyLarge,
                            partOfSpeech = partOfSpeech,
                            partOfSpeechStyle = MaterialTheme.typography.labelMedium,
                            gloss = gloss,
                            glossStyle = MaterialTheme.typography.bodyLarge
                        )

                        BookmarkIconButton(
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState("bookmark-$entryId"),
                                animatedVisibilityScope = animatedContentScope
                            ),
                            isBookmark = isBookmark,
                            onClick = onBookmark
                        )
                    }

                    listOfNotNull(
                        definitionEN,
                        definitionBN,
                        definitionBNIPA,
                        definitionNagri.takeIf { showNagri },
                        definitionIPA
                    ).takeIf { it.isNotEmpty() }?.let {
                        Column(Modifier.padding(horizontal = 16.dp)) {
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
                                        definitionBN?.let {
                                            StringWithFont(it, bengaliBodyFontFamily)
                                        },
                                        definitionBNIPA?.let {
                                            StringWithFont(it, latinBodyFontFamily)
                                        }
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
                                    bodies = definitions.map {
                                        StringWithFont(it, latinBodyFontFamily)
                                    },
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
