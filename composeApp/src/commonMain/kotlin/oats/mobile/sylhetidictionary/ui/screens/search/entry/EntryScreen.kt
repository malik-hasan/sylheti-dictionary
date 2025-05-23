package oats.mobile.sylhetidictionary.ui.screens.search.entry

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.data.dictionary.models.displayEN
import oats.mobile.sylhetidictionary.data.dictionary.models.displayIPA
import oats.mobile.sylhetidictionary.data.dictionary.models.displaySN
import oats.mobile.sylhetidictionary.data.dictionary.models.toDictionaryEntry
import oats.mobile.sylhetidictionary.ui.components.BookmarkIconButton
import oats.mobile.sylhetidictionary.ui.components.EntryCard
import oats.mobile.sylhetidictionary.ui.components.EntryDefinitions
import oats.mobile.sylhetidictionary.ui.components.EntryDivider
import oats.mobile.sylhetidictionary.ui.components.EntryExample
import oats.mobile.sylhetidictionary.ui.components.EntryHeader
import oats.mobile.sylhetidictionary.ui.components.EntrySubHeader
import oats.mobile.sylhetidictionary.ui.components.EntryVariant
import oats.mobile.sylhetidictionary.ui.components.FieldTag
import oats.mobile.sylhetidictionary.ui.components.ReferenceButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBar
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBarWindowInsets
import oats.mobile.sylhetidictionary.ui.components.SearchIconButton
import oats.mobile.sylhetidictionary.ui.components.UpIconButton
import oats.mobile.sylhetidictionary.ui.screens.search.LocalAnimatedContentScope
import oats.mobile.sylhetidictionary.ui.screens.search.LocalSharedTransitionScope
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily
import oats.mobile.sylhetidictionary.ui.utils.copy
import oats.mobile.sylhetidictionary.ui.utils.space
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.component_lexemes
import sylhetidictionary.composeapp.generated.resources.examples
import sylhetidictionary.composeapp.generated.resources.related_words
import sylhetidictionary.composeapp.generated.resources.variants

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun EntryScreen(
    state: EntryState,
    onEvent: (EntryEvent) -> Unit,
    navigateUp: () -> Unit,
    popToSearchBar: () -> Unit,
    navigateToEntry: (entryId: String) -> Unit,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current
) {
    with(sharedTransitionScope) {
        state.entry?.let { entry ->
            with(entry) {
                SDScreen(
                    topBar = {
                        val containerCornerRounding by animatedContentScope.transition.animateDp { state ->
                            when (state) {
                                EnterExitState.PreEnter -> 24.dp
                                EnterExitState.Visible -> 0.dp
                                EnterExitState.PostExit -> 24.dp
                            }
                        }

                        Column(Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("container-$entryId"),
                                animatedVisibilityScope = animatedContentScope,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                                clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(containerCornerRounding))
                            ).background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .windowInsetsPadding(SDTopAppBarWindowInsets)
                        ) {
                            with(animatedContentScope) {
                                SDTopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                                    navigationIcon = {
                                        UpIconButton(
                                            navigateUp = navigateUp,
                                            modifier = Modifier.animateEnterExit(
                                                enter = fadeIn(tween(delayMillis = 300)),
                                                exit = fadeOut(tween(10))
                                            )
                                        )
                                    },
                                    actions = {
                                        SearchIconButton(
                                            popToSearchBar = popToSearchBar,
                                            modifier = Modifier
                                                .animateEnterExit(
                                                    enter = fadeIn(tween(delayMillis = 300)),
                                                    exit = fadeOut(tween(10))
                                                )
                                        )

                                        BookmarkIconButton(
                                            modifier = Modifier.sharedElement(
                                                rememberSharedContentState("bookmark-$entryId"),
                                                animatedVisibilityScope = animatedContentScope
                                            ),
                                            isBookmark = state.isBookmark
                                        ) { onEvent(EntryEvent.Bookmark(entryId, !state.isBookmark)) }
                                    }
                                )
                            }

                            EntryHeader(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp),
                                entryId = entryId,
                                displayIPA = displayIPA,
                                displayEN = displayEN,
                                displaySN = displaySN,
                                displayStyle = MaterialTheme.typography.headlineMedium,
                                partOfSpeech = partOfSpeech,
                                partOfSpeechStyle = MaterialTheme.typography.titleMedium,
                                gloss = gloss,
                                glossStyle = MaterialTheme.typography.titleLarge
                            )

                            Spacer(Modifier
                                .fillMaxWidth()
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("definition-divider-$entryId"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                            )
                        }
                    }
                ) { scaffoldPadding ->
                    val componentEntryIds = remember(state.componentEntries) {
                        state.componentEntries.map { it.entryId }
                    }

                    LazyColumn(
                        modifier = Modifier.padding(
                            scaffoldPadding.copy(bottom = 0.dp)
                        ),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp + scaffoldPadding.calculateBottomPadding()
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.referenceEntries) {
                            ReferenceButton(
                                referenceEntry = it,
                                entryId = entryId,
                                navigateToEntry = navigateToEntry
                            )
                        }

                        val definitions = listOfNotNull(
                            definitionEnglish,
                            definitionBengali,
                            definitionBengaliIPA,
                            definitionSN,
                            definitionIPA
                        )
                        if (definitions.isNotEmpty()) {
                            item { EntryDefinitions(entry) }
                        }

                        if (state.variants.isNotEmpty()) {
                            item {
                                if (definitions.isNotEmpty()) {
                                    EntryDivider(Modifier.padding(bottom = 8.dp))
                                }

                                EntrySubHeader(AnnotatedString(stringResource(Res.string.variants)))

                                Column (Modifier.padding(horizontal = 16.dp)) {
                                    state.variants.forEachIndexed { i, variant ->
                                        EntryVariant(variant)
                                    }
                                }
                            }
                        }

                        itemsIndexed(state.examples) { i, example ->
                            if (i == 0) {
                                if (definitions.isNotEmpty() || state.variants.isNotEmpty()) {
                                    EntryDivider(Modifier.padding(bottom = 8.dp))
                                }

                                EntrySubHeader(AnnotatedString(stringResource(Res.string.examples)))
                            }

                            EntryExample(
                                example = example,
                                index = i,
                                showIndex = state.examples.size > 1
                            )
                        }

                        itemsIndexed(
                            items = state.componentEntries,
                            key = { _, componentEntry -> componentEntry.entryId + "Component" }
                        ) { i, componentEntry ->
                            if (i == 0) {
                                if (definitions.isNotEmpty() || state.variants.isNotEmpty() || state.examples.isNotEmpty()) {
                                    EntryDivider(Modifier.padding(bottom = 8.dp))
                                }

                                EntrySubHeader(
                                    text = buildAnnotatedString {
                                        append(stringResource(Res.string.component_lexemes))

                                        componentEntry.complexFormType
                                            .takeIf { it != "Unspecified Complex Form" }
                                            ?.split(" ")
                                            ?.joinToString(" ") {
                                                it.replaceFirstChar(Char::titlecase)
                                            }?.let { complexFormType ->
                                                space()
                                                withStyle(SpanStyle(fontFamily = latinDisplayFontFamily)) {
                                                    append("($complexFormType)")
                                                }
                                            }
                                    }
                                )
                            }

                            EntryCard(
                                entry = componentEntry.toDictionaryEntry(),
                                navigateToEntry = navigateToEntry,
                                setBookmark = { value ->
                                    onEvent(EntryEvent.Bookmark(componentEntry.entryId, value))
                                }
                            )
                        }

                        itemsIndexed(
                            items = state.relatedEntries,
                            key = { _, relatedEntry -> relatedEntry.entryId + "Related" }
                        ) { i, relatedEntry ->
                            if (i == 0) {
                                if (definitions.isNotEmpty() || state.variants.isNotEmpty() || state.examples.isNotEmpty() || state.componentEntries.isNotEmpty()) {
                                    EntryDivider(Modifier.padding(bottom = 8.dp))
                                }

                                EntrySubHeader(AnnotatedString(stringResource(Res.string.related_words)))
                            }

                            FieldTag(
                                tag = relatedEntry.relationType,
                                tagFontFamily = latinDisplayFontFamily,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            EntryCard(
                                entry = relatedEntry.toDictionaryEntry(),
                                includeAnimation = relatedEntry.entryId !in componentEntryIds,
                                navigateToEntry = navigateToEntry,
                                setBookmark = { value ->
                                    onEvent(EntryEvent.Bookmark(relatedEntry.entryId, value))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
