package ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import models.Route
import models.displayBengali
import models.displayIPA
import models.displayNagri
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.VariantEntry
import ui.app.LocalNavController
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalSharedTransitionScope
import ui.utils.ifTrue

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EntryCard(
    entry: DictionaryEntry,
    isBookmark: Boolean,
    variantEntries: List<VariantEntry>,
    modifier: Modifier = Modifier,
    includeAnimation: Boolean = true,
    navController: NavController = LocalNavController.current,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current,
    onBookmark: () -> Unit
) {
    with(sharedTransitionScope) {
        with(entry) {
            Card(modifier
                .clip(CardDefaults.shape)
                .clickable { navController.navigate(Route.Entry(entry.entryId)) }
                .ifTrue(includeAnimation) {
                    sharedElement(
                        state = rememberSharedContentState("container-$entryId"),
                        animatedVisibilityScope = animatedContentScope
                    )
                }
            ) {
                Column(Modifier.padding(bottom = 12.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        EntryHeader(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp, top = 8.dp)
                                .ifTrue(includeAnimation) {
                                    sharedBounds(
                                        sharedContentState = rememberSharedContentState("header-$entryId"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                },
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
                            modifier = Modifier.ifTrue(includeAnimation) {
                                sharedElement(
                                    state = rememberSharedContentState("bookmark-$entryId"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                            },
                            isBookmark = isBookmark,
                            onClick = onBookmark
                        )
                    }

                    EntryDefinitions(
                        entry = entry,
                        showDivider = true,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .ifTrue(includeAnimation) {
                                sharedBounds(
                                    sharedContentState = rememberSharedContentState("definitions-$entryId"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                            }
                    )

                    variantEntries?.forEach { SeeVariantButton(it) }
                }
            }
        }
    }
}
