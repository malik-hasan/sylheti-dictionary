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
import models.settings.Language
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.example
import sylhetidictionary.composeapp.generated.resources.sylheti
import ui.app.LocalLanguage
import ui.app.LocalShowNagri
import ui.components.BookmarkIconButton
import ui.components.EntryHeader
import ui.components.SDScreen
import ui.components.SearchIconButton
import ui.components.TaggedField
import ui.components.UpIconButton
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.screens.search.LocalSharedTransitionScope
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.utils.StringWithFont

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
    navHostAnimatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current,
    language: Language = LocalLanguage.current,
    showNagri: Boolean = LocalShowNagri.current,
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
                                animatedVisibilityScope = navHostAnimatedContentScope
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
                                            animatedVisibilityScope = navHostAnimatedContentScope
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
                                        animatedVisibilityScope = navHostAnimatedContentScope
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
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
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
                                            StringWithFont(
                                                it,
                                                bengaliBodyFontFamily
                                            )
                                        },
                                        definitionBNIPA?.let {
                                            StringWithFont(
                                                it,
                                                latinBodyFontFamily
                                            )
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
                                        StringWithFont(
                                            it,
                                            latinBodyFontFamily
                                        )
                                    },
                                    highlightRegex = mappedIpaHighlightRegex
                                )
                            }

                            state.examples.forEachIndexed { i, example ->
                                Column {
                                    Text(stringResource(Res.string.example, i + 1))

                                    with(example) {
                                        listOfNotNull(
                                            exampleBengali,
                                            exampleNagri.takeIf { showNagri },
                                            exampleIPA
                                        ).takeIf { it.isNotEmpty() }?.let {
                                            TaggedField(
                                                tag = StringWithFont(stringResource(Res.string.sylheti)),
                                                bodies = listOfNotNull(
                                                    exampleBengali?.let {
                                                        StringWithFont(
                                                            it,
                                                            bengaliBodyFontFamily
                                                        )
                                                    },
                                                    exampleNagri.takeIf { showNagri }
                                                        ?.let(::StringWithFont),
                                                    exampleIPA?.let {
                                                        StringWithFont(
                                                            it,
                                                            latinBodyFontFamily
                                                        )
                                                    }
                                                ),
                                                highlightRegex = mappedIpaHighlightRegex
                                            )
                                        }

                                        exampleEN?.let { example ->
                                            TaggedField(
                                                tag = StringWithFont(stringResource(Res.string.english)),
                                                body = StringWithFont(example, latinBodyFontFamily),
                                                highlightRegex = highlightRegex
                                            )
                                        }

                                        listOfNotNull(
                                            exampleBN,
                                            exampleBNIPA
                                        ).takeIf { it.isNotEmpty() }?.let {
                                            TaggedField(
                                                tag = StringWithFont(stringResource(Res.string.bengali)),
                                                bodies = listOfNotNull(
                                                    exampleBN?.let {
                                                        StringWithFont(
                                                            it,
                                                            bengaliBodyFontFamily
                                                        )
                                                    },
                                                    exampleBNIPA?.let {
                                                        StringWithFont(
                                                            it,
                                                            latinBodyFontFamily
                                                        )
                                                    }
                                                ),
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
        }
    }
}
