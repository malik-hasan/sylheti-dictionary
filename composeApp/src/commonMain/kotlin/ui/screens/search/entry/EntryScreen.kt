@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.screens.search.entry

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import models.search.settings.SearchScript
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ui.components.BookmarkIconButton
import ui.components.Chip
import ui.components.EntryCard
import ui.components.EntryDefinitions
import ui.components.EntryDivider
import ui.components.EntryExample
import ui.components.EntryHeader
import ui.components.SDScreen
import ui.components.SearchIconButton
import ui.components.TaggedField
import ui.components.UpIconButton
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.screens.search.LocalSharedTransitionScope
import ui.utils.SDString
import utility.isPrimaryComponent
import utility.toDictionaryEntry

@Composable
fun EntryScreen(
    entryId: String,
    vm: EntryViewModel = koinViewModel { parametersOf(entryId) }
) = with(vm) {
    val state by state.collectAsStateWithLifecycle()

    EntryScreen(state, ::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    state: EntryState,
    onEvent: (EntryEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current
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
                                    ) { onEvent(EntryEvent.ToggleBookmark) }
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
                        itemsIndexed(state.variants) { i, variant ->
                            TaggedField(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                tag = variant.environment ?: "variant",
                                bodies = listOfNotNull(
                                    SDString(variant.variantIPA, mappedIpaHighlightRegex, SearchScript.LATIN),
                                    variant.variantBengali?.let { SDString(it, highlightRegex, SearchScript.BENGALI) },
                                    variant.variantNagri?.let { SDString(it, highlightRegex) },
                                ),
                                separator = " • "
                            )

                            if (i == state.variants.lastIndex) {
                                EntryDivider(Modifier.padding(vertical = 8.dp))
                            }
                        }

                        item {
                            EntryDefinitions(
                                entry = entry,
                                showDivider = false,
                                definitionStyle = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState("definitions-$entryId"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                            )
                        }

                        itemsIndexed(state.examples) { i, example ->
                            if (i == 0) EntryDivider(Modifier.padding(vertical = 8.dp))
                            EntryExample(example, i, Modifier.padding(horizontal = 16.dp))
                        }

                        itemsIndexed(
                            items = state.componentLexemes,
                            key = { _, componentEntry -> componentEntry.entryId + "Component" }
                        ) { i, componentEntry ->
                            if (i == 0) {
                                EntryDivider(Modifier.padding(vertical = 8.dp))
                                Text(
                                    text = "Component Lexemes",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            if (componentEntry.isPrimaryComponent) {
                                Chip("Primary")
                            }
                            componentEntry.complexFormType?.let {
                                Text(it, style = MaterialTheme.typography.bodySmall)
                            }
                            componentEntry.variantType?.let {
                                Text(it, style = MaterialTheme.typography.bodySmall)
                            }
                            EntryCard(componentEntry.toDictionaryEntry(), false) {}
                        }

                        itemsIndexed(
                            items = state.relatedEntries,
                            key = { _, relatedEntry -> relatedEntry.entryId + "Related" }
                        ) { i, relatedEntry ->
                            if (i == 0) {
                                EntryDivider(Modifier.padding(vertical = 8.dp))
                                Text(
                                    text = "Related Entries",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            Text(relatedEntry.relationType, style = MaterialTheme.typography.bodySmall)
                            EntryCard(
                                entry = relatedEntry.toDictionaryEntry(),
                                isBookmark = false,
                                includeAnimation = relatedEntry.entryId !in state.componentLexemes.map { it.entryId }
                            ) {}
                        }
                    }
                }
            }
        }
    }
}
