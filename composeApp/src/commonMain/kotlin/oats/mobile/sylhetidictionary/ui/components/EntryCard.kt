package oats.mobile.sylhetidictionary.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryRepository
import oats.mobile.sylhetidictionary.models.Route
import oats.mobile.sylhetidictionary.models.displayEN
import oats.mobile.sylhetidictionary.models.displayIPA
import oats.mobile.sylhetidictionary.models.displaySN
import oats.mobile.sylhetidictionary.ui.app.LocalNavController
import oats.mobile.sylhetidictionary.ui.screens.search.LocalAnimatedContentScope
import oats.mobile.sylhetidictionary.ui.screens.search.LocalSharedTransitionScope
import oats.mobile.sylhetidictionary.ui.utils.ifTrue
import org.koin.compose.koinInject

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EntryCard(
    entry: DictionaryEntry,
    modifier: Modifier = Modifier,
    includeAnimation: Boolean = true,
    dictionaryRepository: DictionaryRepository = koinInject(),
    bookmarksRepository: BookmarksRepository = koinInject(),
    navController: NavController = LocalNavController.current,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current,
    setBookmark: (Boolean) -> Unit
) {
    val isBookmark by bookmarksRepository.isBookmarkFlow(entry.entryId).collectAsStateWithLifecycle(false)

    var referenceEntries by remember { mutableStateOf(emptyList<DictionaryEntry>()) }
    LaunchedEffect(entry) {
        referenceEntries = dictionaryRepository.getReferenceEntries(entry.entryId)
    }

    with(sharedTransitionScope) {
        with(entry) {
            var isNavigatingToThisEntry by remember { mutableStateOf(false) }

            val containerCornerRounding by animatedContentScope.transition
                .animateDp(label = "containerCornerAnimation") { state ->
                    if (isNavigatingToThisEntry) {
                        when (state) {
                            EnterExitState.PreEnter -> 0.dp
                            EnterExitState.Visible -> 24.dp
                            EnterExitState.PostExit -> 0.dp
                        }
                    } else 24.dp
                }

            val cardShape = RoundedCornerShape(containerCornerRounding)
            Card(modifier
                .clip(cardShape)
                .clickable {
                    isNavigatingToThisEntry = true
                    navController.navigate(Route.Entry(entry.entryId))
                }.ifTrue(includeAnimation) {
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
                            displayEN = displayEN,
                            displaySN = displaySN,
                            displayStyle = MaterialTheme.typography.bodyLarge,
                            partOfSpeech = partOfSpeech,
                            partOfSpeechStyle = MaterialTheme.typography.labelMedium,
                            gloss = gloss,
                            glossStyle = MaterialTheme.typography.bodyLarge
                        )

                        BookmarkIconButton(
                            modifier = Modifier.ifTrue(includeAnimation) {
                                sharedElement(
                                    rememberSharedContentState("bookmark-$entryId"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                            },
                            isBookmark = isBookmark,
                            onClick = { setBookmark(!isBookmark) }
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

                    referenceEntries.forEach {
                        ReferenceButton(
                            referenceEntry = it,
                            entryId = entryId,
                            includeAnimation = includeAnimation
                        )
                    }
                }
            }
        }
    }
}
