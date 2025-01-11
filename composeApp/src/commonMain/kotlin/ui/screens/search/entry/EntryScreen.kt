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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ui.components.BookmarkIconButton
import ui.components.EntryCard
import ui.components.EntryDefinitions
import ui.components.EntryDivider
import ui.components.EntryExample
import ui.components.EntryHeader
import ui.components.SDScreen
import ui.components.SearchIconButton
import ui.components.UpIconButton
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalSharedTransitionScope

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
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            EntryDefinitions(
                                entry = entry,
                                showDivider = false,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp).padding(top = 8.dp)
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

                        itemsIndexed(state.variants) { i, variant ->
                            if (i == 0) EntryDivider()
                            Text("Variant ${variant.variantBengali} ${variant.variantIPA}")
                        }

                        itemsIndexed(state.componentLexemes) { i, componentEntry ->
                            if (i == 0) EntryDivider()
                            EntryCard(componentEntry, false) {}
                        }

                        itemsIndexed(state.relatedEntries) { i, relatedEntry ->
                            if (i == 0) EntryDivider()
                            EntryCard(relatedEntry, false) {}
                        }
                    }
                }
            }
        }
    }
}
