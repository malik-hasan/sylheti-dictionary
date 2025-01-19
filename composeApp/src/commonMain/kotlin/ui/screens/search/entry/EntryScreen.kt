@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.screens.search.entry

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.component_lexemes
import sylhetidictionary.composeapp.generated.resources.examples
import sylhetidictionary.composeapp.generated.resources.related_words
import sylhetidictionary.composeapp.generated.resources.variants
import ui.components.BookmarkIconButton
import ui.components.EntryCard
import ui.components.EntryDefinitions
import ui.components.EntryDivider
import ui.components.EntryExample
import ui.components.EntryHeader
import ui.components.EntrySubHeader
import ui.components.EntryVariant
import ui.components.FieldTag
import ui.components.SDScreen
import ui.components.SearchIconButton
import ui.components.SeeVariantButton
import ui.components.UpIconButton
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalSharedTransitionScope
import ui.theme.latinDisplayFontFamily

@Composable
fun EntryScreen(
    entryId: String,
    vm: EntryViewModel = koinViewModel { parametersOf(entryId) }
) = with(vm) {
    val state by state.collectAsStateWithLifecycle()

    EntryScreen(state, ::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun EntryScreen(
    state: EntryState,
    onEvent: (EntryEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current
) {
    with(sharedTransitionScope) {
        state.entry?.let { entry ->
            with(entry) {
                SDScreen(
                    topBar = {
                        Column(Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .sharedElement(
                                state = rememberSharedContentState("container-$entryId"),
                                animatedVisibilityScope = animatedContentScope
                            )
                        ) {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                                navigationIcon = { UpIconButton() },
                                title = {},
                                actions = {
                                    SearchIconButton()

                                    BookmarkIconButton(
                                        modifier = Modifier.sharedElement(
                                            state = rememberSharedContentState("bookmark-$entryId"),
                                            animatedVisibilityScope = animatedContentScope
                                        ),
                                        isBookmark = state.isBookmark
                                    ) { onEvent(EntryEvent.Bookmark(entryId, !state.isBookmark)) }
                                }
                            )

                            EntryHeader(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp)
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState("header-$entryId"),
                                        animatedVisibilityScope = animatedContentScope
                                    ),
                                displayIPA = citationIPA ?: lexemeIPA,
                                displayBengali = citationBengali ?: lexemeBengali,
                                displayNagri = citationNagri ?: lexemeNagri,
                                displayStyle = MaterialTheme.typography.headlineMedium,
                                partOfSpeech = partOfSpeech,
                                partOfSpeechStyle = MaterialTheme.typography.titleMedium,
                                gloss = gloss,
                                glossStyle = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.variantEntries) { variantEntry ->
                            SeeVariantButton(variantEntry)
                        }

                        val definitions = listOfNotNull(
                            definitionEN,
                            definitionBN,
                            definitionBNIPA,
                            definitionNagri,
                            definitionIPA
                        )
                        if (definitions.isNotEmpty()) {
                            item {
                                EntryDefinitions(
                                    entry = entry,
                                    showDivider = false,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .sharedBounds(
                                            sharedContentState = rememberSharedContentState("definitions-$entryId"),
                                            animatedVisibilityScope = animatedContentScope
                                        )
                                )
                            }
                        }

                        itemsIndexed(state.variants) { i, variant ->
                            if (i == 0) {
                                if (definitions.isNotEmpty()) {
                                    EntryDivider(Modifier.padding(bottom = 8.dp))
                                }

                                EntrySubHeader(stringResource(Res.string.variants))
                            }

                            EntryVariant(
                                variant = variant,
                                index = i,
                                showIndex = state.variants.size > 1
                            )
                        }

                        itemsIndexed(state.examples) { i, example ->
                            if (i == 0) {
                                if (definitions.isNotEmpty() || state.variants.isNotEmpty()) {
                                    EntryDivider(Modifier.padding(bottom = 8.dp))
                                }

                                EntrySubHeader(stringResource(Res.string.examples))
                            }

                            EntryExample(
                                example = example,
                                index = i,
                                showIndex = state.examples.size > 1
                            )
                        }

                        itemsIndexed(
                            items = state.componentLexemes.toList(),
                            key = { _, (componentEntry) -> componentEntry.entryId + "Component" }
                        ) { i, (componentEntry, cardEntry) ->
                            if (i == 0) {
                                if (definitions.isNotEmpty() || state.variants.isNotEmpty() || state.examples.isNotEmpty()) {
                                    EntryDivider(Modifier.padding(bottom = 8.dp))
                                }

                                val complexFormType = componentEntry.complexFormType
                                    .takeIf { it != "Unspecified Complex Form" }
                                    ?.split(" ")
                                    ?.joinToString(" ") {
                                        it.replaceFirstChar(Char::titlecase)
                                    }?.let {
                                        "($it)"
                                    }.orEmpty()

                                EntrySubHeader(stringResource(Res.string.component_lexemes, complexFormType))
                            }

                            with(cardEntry) {
                                EntryCard(
                                    entry = dictionaryEntry,
                                    isBookmark = isBookmark,
                                    variantEntries = variantEntries,
                                ) { onEvent(EntryEvent.Bookmark(componentEntry.entryId, !isBookmark)) }
                            }
                        }

                        itemsIndexed(
                            items = state.relatedEntries.toList(),
                            key = { _, (relatedEntry) -> relatedEntry.entryId + "Related" }
                        ) { i, (relatedEntry, cardEntry) ->
                            if (i == 0) {
                                if (definitions.isNotEmpty() || state.variants.isNotEmpty() || state.examples.isNotEmpty() || state.componentLexemes.isNotEmpty()) {
                                    EntryDivider(Modifier.padding(bottom = 8.dp))
                                }

                                EntrySubHeader(stringResource(Res.string.related_words))
                            }

                            FieldTag(
                                tag = relatedEntry.relationType.takeIf { it != "Synonyms" } ?: "Synonym",
                                tagFontFamily = latinDisplayFontFamily,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            with(cardEntry) {
                                EntryCard(
                                    entry = dictionaryEntry,
                                    isBookmark = isBookmark,
                                    variantEntries = variantEntries,
                                    includeAnimation = relatedEntry.entryId !in state.componentLexemes.map { it.key.entryId }
                                ) { onEvent(EntryEvent.Bookmark(relatedEntry.entryId, !isBookmark)) }
                            }
                        }
                    }
                }
            }
        }
    }
}
