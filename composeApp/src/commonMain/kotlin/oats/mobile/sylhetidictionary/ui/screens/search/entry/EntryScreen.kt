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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import oats.mobile.sylhetidictionary.ui.components.BackIconButton
import oats.mobile.sylhetidictionary.ui.components.BookmarkIconButton
import oats.mobile.sylhetidictionary.ui.components.EntryCard
import oats.mobile.sylhetidictionary.ui.components.EntryDefinitions
import oats.mobile.sylhetidictionary.ui.components.EntryExample
import oats.mobile.sylhetidictionary.ui.components.EntryHeader
import oats.mobile.sylhetidictionary.ui.components.EntrySubHeader
import oats.mobile.sylhetidictionary.ui.components.EntryVariant
import oats.mobile.sylhetidictionary.ui.components.ReferenceButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBar
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBarWindowInsets
import oats.mobile.sylhetidictionary.ui.components.SearchIcon
import oats.mobile.sylhetidictionary.ui.components.TaggedEntryCard
import oats.mobile.sylhetidictionary.ui.screens.search.LocalAnimatedContentScope
import oats.mobile.sylhetidictionary.ui.screens.search.LocalSharedTransitionScope
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily
import oats.mobile.sylhetidictionary.ui.utils.copy
import oats.mobile.sylhetidictionary.ui.utils.space
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.component_lexemes
import sylhetidictionary.composeapp.generated.resources.derivative_lexemes
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
                                        BackIconButton(
                                            onClick = navigateUp,
                                            modifier = Modifier.animateEnterExit(
                                                enter = fadeIn(tween(delayMillis = 300)),
                                                exit = fadeOut(tween(10))
                                            )
                                        )
                                    },
                                    actions = {
                                        IconButton(
                                            onClick = popToSearchBar,
                                            modifier = Modifier
                                                .animateEnterExit(
                                                    enter = fadeIn(tween(delayMillis = 300)),
                                                    exit = fadeOut(tween(10))
                                                )
                                        ) { SearchIcon() }

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
                    val lazyListState = rememberLazyListState()

                    val visibleItemKeys by derivedStateOf {
                        lazyListState.layoutInfo.visibleItemsInfo.mapNotNullTo(HashSet()) { it.key as? String }
                    }

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.padding(scaffoldPadding.copy(bottom = 0.dp)),
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            bottom = 16.dp + scaffoldPadding.calculateBottomPadding()
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.referenceEntries,
                            key = { "reference::${it.entryId}" },
                            contentType = { "reference button" }
                        ) {
                            ReferenceButton(
                                referenceEntry = it,
                                entryId = entryId,
                                navigateToEntry = navigateToEntry,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .animateItem()
                            )
                        }

                        listOfNotNull(
                            definitionEnglish,
                            definitionBengali,
                            definitionBengaliIPA,
                            definitionSN,
                            definitionIPA
                        ).takeIf { it.isNotEmpty() }?.let {
                            item(key = "definitions", contentType = "definitions") {
                                EntryDefinitions(
                                    entry = entry,
                                    featureBengaliDefinitions = state.featureBengaliDefinitions,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .animateItem()
                                )
                            }
                        }

                        val subHeaderContentType = "subheader"
                        if (state.variants.isNotEmpty()) {
                            stickyHeader(key = "variants header", contentType = subHeaderContentType) {
                                EntrySubHeader(
                                    text = AnnotatedString(stringResource(Res.string.variants)),
                                    onClick = { onEvent(EntryEvent.ToggleVariants) },
                                    expanded = state.variantsExpanded
                                )
                            }

                            if (state.variantsExpanded) {
                                item(key = "variants", contentType = "variants") {
                                    Column(Modifier
                                        .padding(horizontal = 32.dp)
                                        .animateItem()
                                    ) {
                                        state.variants.forEach {
                                            EntryVariant(it)
                                        }
                                    }
                                }
                            }
                        }

                        if (state.examples.isNotEmpty()) {
                            stickyHeader(key = "examples header", contentType = subHeaderContentType) {
                                EntrySubHeader(
                                    text = AnnotatedString(stringResource(Res.string.examples)),
                                    onClick = { onEvent(EntryEvent.ToggleExamples) },
                                    expanded = state.examplesExpanded
                                )
                            }
                        }

                        if (state.examplesExpanded) {
                            itemsIndexed(
                                items = state.examples,
                                key = { _, example -> "example::${example.id}" },
                                contentType = { _, _ -> "example" }
                            ) { i, example ->
                                EntryExample(
                                    example = example,
                                    index = i,
                                    showIndex = state.examples.size > 1,
                                    featureBengaliExamples = state.featureBengaliExamples,
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }

                        if (state.componentEntries.isNotEmpty()) {
                            stickyHeader(key = "component lexemes header", contentType = subHeaderContentType) {
                                EntrySubHeader(
                                    text = buildAnnotatedString {
                                        append(stringResource(Res.string.component_lexemes))

                                        state.componentEntries.first().complexFormType
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
                                    },
                                    onClick = { onEvent(EntryEvent.ToggleComponentLexemes) },
                                    expanded = state.componentEntriesExpanded
                                )
                            }
                        }

                        if (state.componentEntriesExpanded) {
                            items(
                                items = state.componentEntries,
                                key = { "component::${it.entryId}" },
                                contentType = { "entry card" }
                            ) { componentEntry ->
                                EntryCard(
                                    entry = componentEntry.toDictionaryEntry(),
                                    featureBengaliDefinitions = state.featureBengaliDefinitions,
                                    navigateToEntry = navigateToEntry,
                                    setBookmark = { value ->
                                        onEvent(EntryEvent.Bookmark(componentEntry.entryId, value))
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .animateItem()
                                )
                            }
                        }

                        if (state.derivativeEntries.isNotEmpty()) {
                            stickyHeader(key = "derivative lexemes header", contentType = subHeaderContentType) {
                                EntrySubHeader(
                                    text = AnnotatedString(stringResource(Res.string.derivative_lexemes)),
                                    onClick = { onEvent(EntryEvent.ToggleDerivativeLexemes) },
                                    expanded = state.derivativeEntriesExpanded
                                )
                            }
                        }

                        val taggedEntryCardContentType = "tagged entry card"
                        if (state.derivativeEntriesExpanded) {
                            items(
                                items = state.derivativeEntries,
                                key = { "derivative::${it.entryId}" },
                                contentType = { taggedEntryCardContentType }
                            ) { derivativeEntry ->
                                TaggedEntryCard(
                                    tag = derivativeEntry.complexFormType.takeIf { it != "Unspecified Complex Form" },
                                    entry = derivativeEntry.toDictionaryEntry(),
                                    featureBengaliDefinitions = state.featureBengaliDefinitions,
                                    includeAnimation = "component::${derivativeEntry.entryId}" !in visibleItemKeys,
                                    navigateToEntry = navigateToEntry,
                                    setBookmark = { value ->
                                        onEvent(EntryEvent.Bookmark(derivativeEntry.entryId, value))
                                    }
                                )
                            }
                        }

                        if (state.relatedEntries.isNotEmpty()) {
                            stickyHeader(key = "related words header", contentType = subHeaderContentType) {
                                EntrySubHeader(
                                    text = AnnotatedString(stringResource(Res.string.related_words)),
                                    onClick = { onEvent(EntryEvent.ToggleRelatedWords) },
                                    expanded = state.relatedEntriesExpanded
                                )
                            }
                        }

                        if (state.relatedEntriesExpanded) {
                            items(
                                items = state.relatedEntries,
                                key = { "related::${it.entryId}" },
                                contentType = { taggedEntryCardContentType }
                            ) { relatedEntry ->
                                TaggedEntryCard(
                                    tag = relatedEntry.relationType,
                                    entry = relatedEntry.toDictionaryEntry(),
                                    featureBengaliDefinitions = state.featureBengaliDefinitions,
                                    includeAnimation = "component::${relatedEntry.entryId}" !in visibleItemKeys
                                        && "derivative::${relatedEntry.entryId}" !in visibleItemKeys,
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
}
