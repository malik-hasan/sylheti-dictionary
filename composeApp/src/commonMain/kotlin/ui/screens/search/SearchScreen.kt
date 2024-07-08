package ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import models.search.settings.LatinSearchLanguage
import models.search.settings.SearchPosition
import models.search.settings.SearchScript
import ui.components.EntryCard
import ui.ifTrue
import ui.interfaces.LocalDrawerState
import ui.isScrollingUp
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(vm: SearchViewModel = viewModel()) {
    SearchScreen(vm.state, vm::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit
) {
    LaunchedEffect(state.searchResults) {
        if (state.searchResults == null) onEvent(SearchEvent.LoadFavorites)
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var dstate by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val scope = rememberCoroutineScope()
            val drawerState = LocalDrawerState.current
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text("Sylheti Dictionary")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                with(drawerState) {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Menu, "menu")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { dstate = true }) {
                            Icon(Icons.Default.Tune, "settings")
                        }

                        DropdownMenu(
                            expanded = dstate,
                            onDismissRequest = { dstate = !dstate }
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Parts of word to search")
                                MultiChoiceSegmentedButtonRow {
                                    val searchPositions = SearchPosition.entries
                                    searchPositions.forEachIndexed { index, searchPosition ->
                                        SegmentedButton(
                                            checked = true,
                                            onCheckedChange = {},
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = index,
                                                count = searchPositions.size
                                            )
                                        ) {
                                            Text(searchPosition.name)
                                        }
                                    }
                                }
                                Text("Search script")
                                SingleChoiceSegmentedButtonRow {
                                    val searchScripts = SearchScript.entries
                                    searchScripts.forEachIndexed { index, searchScript ->
                                        SegmentedButton(
                                            selected = false,
                                            onClick = {},
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = index,
                                                count = searchScripts.size
                                            )
                                        ) {
                                            Text(searchScript.toString())
                                        }
                                    }
                                }
                                Text("Search language")
                                MultiChoiceSegmentedButtonRow {
                                    val searchLanguages = LatinSearchLanguage.entries
                                    searchLanguages.forEachIndexed { index, searchLanguage ->
                                        SegmentedButton(
                                            checked = true,
                                            onCheckedChange = {},
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = index,
                                                count = searchLanguages.size
                                            )
                                        ) {
                                            Text(searchLanguage.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        Box {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 72.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val items = state.searchResults ?: state.favorites

                if (items.isEmpty()) {
                    item { Text("No results") }
                    return@LazyColumn
                }

//                    item(results.first().entry.entryId) {
//                        val data = results.first()
//                        Card(Modifier.fillMaxWidth()) {
//                            Text(text = data.entry.lexemeIPA)
//                            Text(text = data.entry.lexemeBangla ?: "No bangla lexeme")
//                            Text(text = data.entry.lexemeNagri ?: "No nagri lexeme")
//                            Text(text = data.entry.citationIPA ?: "No IPA citation")
//                            Text(text = data.entry.citationBangla ?: "No bangla citation")
//                            Text(text = data.entry.citationNagri ?: "No nagri citation")
//                            Text(text = data.entry.partOfSpeech ?: "POS unknown")
//                            Text(text = data.entry.gloss ?: "?")
//                            Text(text = data.entry.definitionEN ?: "no en def")
//                            Text(text = data.entry.definitionBN ?: "no bn def")
//                            Text(text = data.entry.definitionBNIPA ?: "no bnipa def")
//                            Text(text = data.entry.definitionIPA ?: "no ipa def")
//                            Text(text = data.entry.definitionNagri ?: "no nagri def")
//                            Text(text = data.componentLexemes.toString())
//                            Text(text = data.examples.toString())
//                            Text(text = data.variants.toString())
//                            Text(text = data.domains.toString())
//                            Text(text = data.relatedEntries.size.toString())
//                        }
//
//                    }

                items(items) { data ->
                    EntryCard(data) { entryId, isFavorite ->
                        onEvent(SearchEvent.MarkFavorite(entryId, isFavorite))
                    }
                }
            }

            AnimatedVisibility(
                visible = listState.isScrollingUp.value,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {

                SearchBar(
                    modifier = Modifier
                        .padding(scaffoldPadding)
                        .ifTrue(!state.searchBarActive) { padding(horizontal = 8.dp) }
                        .fillMaxWidth(),
                    shadowElevation = 6.dp,
                    tonalElevation = 50000.dp,
                    windowInsets = WindowInsets(0.dp),
                    placeholder = { Text("Search dictionary") },
                    query = state.searchTerm,
                    onQueryChange = { onEvent(SearchEvent.UpdateSearchTerm(it)) },
                    onSearch = {
                        onEvent(SearchEvent.Search)
                        scope.launch {
                            listState.scrollToItem(0)
                        }
                    },
                    active = state.searchBarActive,
                    onActiveChange = { onEvent(SearchEvent.SetSearchBarActive(it)) },
                    leadingIcon = {
                        if (state.searchBarActive) {
                            IconButton({ onEvent(SearchEvent.SetSearchBarActive(false)) }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "clear")
                            }
                        } else Icon(Icons.Default.Search, "Search")
                    },
                    trailingIcon = {
                        if (state.searchBarActive || state.searchTerm.isNotBlank()) {
                            IconButton({ onEvent(SearchEvent.ClearSearchBar) }) {
                                Icon(Icons.Default.Clear, "clear")
                            }
                        }
                    }
                ) {
                    state.searchResults?.let { results ->
                        LazyColumn {
                            if (results.isEmpty()) {
                                item { Text("No suggestions") }
                            }

                            items(results) { data ->
                                val word = data.entry.lexemeIPA
                                Text(
                                    text = word,
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(
                                                SearchEvent.SelectSuggestion(
                                                    word
                                                )
                                            )
                                        }
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
