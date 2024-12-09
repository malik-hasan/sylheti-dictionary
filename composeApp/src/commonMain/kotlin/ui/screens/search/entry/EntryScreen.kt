package ui.screens.search.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
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
import ui.components.EntrySubHeader
import ui.components.SDScreen
import ui.components.SearchIconButton
import ui.components.TaggedField
import ui.components.UpIconButton
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.utils.StringWithFont

@Composable
fun EntryScreen(
    entryId: String,
    vm: EntryViewModel = koinViewModel { parametersOf(entryId) }
) {
    val state by vm.state.collectAsStateWithLifecycle()

    EntryScreen(state, vm::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    state: EntryState,
    onEvent: (EntryEvent) -> Unit,
    language: Language = LocalLanguage.current,
    density: Density = LocalDensity.current,
    showNagri: Boolean = LocalShowNagri.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current
) {
    SDScreen(
        topBar = {
            var titleHeight by remember { mutableStateOf(TopAppBarDefaults.LargeAppBarExpandedHeight) }

            LargeTopAppBar(
                expandedHeight = maxOf(
                    TopAppBarDefaults.LargeAppBarExpandedHeight,
                    titleHeight + TopAppBarDefaults.LargeAppBarCollapsedHeight
                ),
                navigationIcon = { UpIconButton() },
                title = {
                    state.entry?.let { entry ->
                        Column(Modifier
                            .onGloballyPositioned {
                                titleHeight = with(density) { it.size.height.toDp() }
                            }.padding(bottom = 16.dp)
                        ) {
                            with(entry) {
                                EntryHeader(
                                    displayIPA = citationIPA ?: lexemeIPA,
                                    displayBengali = citationBengali ?: lexemeBengali,
                                    displayNagri = citationNagri ?: lexemeNagri
                                )

                                EntrySubHeader(
                                    partOfSpeech = partOfSpeech,
                                    partOfSpeechStyle = MaterialTheme.typography.titleMedium,
                                    gloss = gloss,
                                    glossStyle = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                },
                actions = {
                    SearchIconButton()

                    BookmarkIconButton(state.isBookmark) {
                        onEvent(EntryEvent.ToggleBookmark)
                    }
                }
            )
        }
    ) { scaffoldPadding ->

        Column(Modifier
            .padding(scaffoldPadding)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.entry?.let { entry ->
                with(entry) {
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
                                definitionBN?.let { StringWithFont(it, bengaliBodyFontFamily) },
                                definitionBNIPA?.let { StringWithFont(it, latinBodyFontFamily) }
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
                }
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
