package ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
            val containerCornerRounding by animatedContentScope.transition
                .animateDp(label = "containerCornerAnimation") { state ->
                    when (state) {
                        EnterExitState.PreEnter -> 0.dp
                        EnterExitState.Visible -> 12.dp
                        EnterExitState.PostExit -> 0.dp
                    }
                }

            val cardShape = RoundedCornerShape(containerCornerRounding)
            Card(modifier
                .clip(cardShape)
                .clickable { navController.navigate(Route.Entry(entry.entryId)) }
                .ifTrue(includeAnimation) {
                    sharedBounds(
                        sharedContentState = rememberSharedContentState("container-$entryId"),
                        animatedVisibilityScope = animatedContentScope,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        clipInOverlayDuringTransition = OverlayClip(cardShape)
                    )
                }
            ) {
                Column(Modifier.padding(bottom = 12.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        EntryHeader(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp, top = 8.dp),
                            entryId = entryId,
                            includeAnimation = includeAnimation,
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

                    EntryDivider(Modifier
                        .padding(horizontal = 16.dp)
                        .ifTrue(includeAnimation) {
                            sharedBounds(
                                sharedContentState = rememberSharedContentState("definition-divider-$entryId"),
                                animatedVisibilityScope = animatedContentScope,
                                enter = EnterTransition.None,
                                exit = ExitTransition.None
                            )
                        }
                    )

                    EntryDefinitions(
                        entry = entry,
                        includeAnimation = includeAnimation
                    )

                    variantEntries.forEach {
                        SeeVariantButton(
                            variantEntry = it,
                            entryId = entryId
                        )
                    }

                    Spacer(Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("expanding-details-$entryId"),
                            animatedVisibilityScope = animatedContentScope,
                        )
                    )
                }
            }
        }
    }
}
