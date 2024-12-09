package ui.screens.search.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import models.displayBengali
import models.displayIPA
import models.settings.Language
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ui.app.LocalLanguage
import ui.components.EntryCard
import ui.components.SDScreen
import ui.components.SearchIconButton
import ui.components.UpIconButton
import ui.screens.settings.SettingsEvent
import ui.theme.latinDisplayFontFamily

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
    onEvent: (SettingsEvent) -> Unit,
    language: Language = LocalLanguage.current
) {
    SDScreen(
        topBar = {
            TopAppBar(
                navigationIcon = { UpIconButton() },
                title = {
                    state.entry?.let { entry ->
                       if (language == Language.BN && entry.displayBengali != null) {
                           Text(
                               text = entry.displayBengali!!,
                               style = MaterialTheme.typography.headlineSmall
                           )
                       } else {
                           Text(
                               text = entry.displayIPA,
                               fontFamily = latinDisplayFontFamily,
                               style = MaterialTheme.typography.headlineSmall
                           )
                       }
                    }
                },
                actions = { SearchIconButton() }
            )
        }
    ) { scaffoldPadding ->

        Box(Modifier
            .padding(scaffoldPadding)
            .verticalScroll(rememberScrollState())
        ) {
            with(state) {
                entry?.let { entry ->
                    EntryCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        entry = entry,
                        isBookmark = isBookmark,
                        onBookmark = {
//                            onSearchEvent(SearchEvent.Bookmark(entry.entryId, !isBookmark))
                        }
                    )
                }
            }

//            expandTransition.AnimatedVisibility(
//                visible = { it },
//                enter = expandVertically() + fadeIn(),
//                exit = shrinkVertically() + fadeOut()
//            ) {
//                HorizontalDivider()
//                Column(
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    extendedEntry.examples.forEachIndexed { i, example ->
//                        Column {
//                            Text(stringResource(Res.string.example, i + 1))
//
//                            with(example) {
//                                listOfNotNull(
//                                    exampleBengali,
//                                    exampleNagri.takeIf { showNagri },
//                                    exampleIPA
//                                ).takeIf { it.isNotEmpty() }?.let {
//                                    TaggedField(
//                                        tag = StringWithFont(stringResource(Res.string.sylheti)),
//                                        bodies = listOfNotNull(
//                                            exampleBengali?.let { StringWithFont(it, bengaliBodyFontFamily) },
//                                            exampleNagri.takeIf { showNagri }?.let(::StringWithFont),
//                                            exampleIPA?.let { StringWithFont(it, latinBodyFontFamily) }
//                                        ),
//                                        highlightRegex = mappedIpaHighlightRegex
//                                    )
//                                }
//
//                                exampleEN?.let { example ->
//                                    TaggedField(
//                                        tag = StringWithFont(stringResource(Res.string.english)),
//                                        body = StringWithFont(example, latinBodyFontFamily),
//                                        highlightRegex = highlightRegex
//                                    )
//                                }
//
//                                listOfNotNull(
//                                    exampleBN,
//                                    exampleBNIPA
//                                ).takeIf { it.isNotEmpty() }?.let {
//                                    TaggedField(
//                                        tag = StringWithFont(stringResource(Res.string.bengali)),
//                                        bodies = listOfNotNull(
//                                            exampleBN?.let { StringWithFont(it, bengaliBodyFontFamily) },
//                                            exampleBNIPA?.let { StringWithFont(it, latinBodyFontFamily) }
//                                        ),
//                                        highlightRegex = mappedIpaHighlightRegex
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}
