package ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.search_dictionary
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.sylheti_dictionary
import sylhetidictionary.composeapp.generated.resources.tune
import ui.components.EntryCard
import ui.components.SearchSettingsMenu
import ui.components.SylhetiDictionaryTopBar
import ui.utils.ifTrue
import ui.utils.isScrollingUp

@Composable
fun SearchScreen(vm: SearchViewModel = koinViewModel()) {
    val assetLoaded by vm.assetLoaded.collectAsStateWithLifecycle()
    val searchState by vm.searchState.collectAsStateWithLifecycle()
    val settingsState by vm.settingsState.collectAsStateWithLifecycle()

    SearchScreen(assetLoaded, searchState, vm::onSearchEvent, settingsState, vm::onSettingsEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    assetLoaded: Boolean,
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    settingsState: SearchSettingsState,
    onSettingsEvent: (SearchSettingsEvent) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SylhetiDictionaryTopBar(stringResource(Res.string.sylheti_dictionary), scrollBehavior) {
                Box {
                    IconButton(onClick = { onSettingsEvent(SearchSettingsEvent.ToggleSettingsMenu(true)) }) {
                        Icon(
                            painterResource(Res.drawable.tune),
                            stringResource(Res.string.settings)
                        )
                    }

                    SearchSettingsMenu(settingsState, onSettingsEvent)
                }
            }
        }
    ) { scaffoldPadding ->

        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        if (!assetLoaded) {
            Text(
                "There was an error loading the dictionary data. Try restarting the app, or report a bug if the problem persists.",
                modifier = Modifier.fillMaxWidth().padding(scaffoldPadding),
                textAlign = TextAlign.Center
            )
            return@Scaffold
        }

        Box {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 72.dp,
                    bottom = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val searchResults = searchState.searchResults
                val bookmarks = searchState.bookmarks

                val items = searchResults?.let {
                    searchResults.ifEmpty {
                        item { Text("No results") }
                        return@LazyColumn
                    }
                } ?: bookmarks.ifEmpty { return@LazyColumn }

                items(items) { entry ->
                    EntryCard(entry) { entryId, isFavorite ->
                        onSearchEvent(SearchEvent.Bookmark(entryId, isFavorite))
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
                        .ifTrue(!searchState.searchBarActive) { padding(horizontal = 8.dp) }
                        .fillMaxWidth(),
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchState.searchTerm,
                            onQueryChange = { onSearchEvent(SearchEvent.UpdateSearchTerm(it)) },
                            onSearch = {
                                onSearchEvent(SearchEvent.Search)
                                scope.launch {
                                    listState.scrollToItem(0)
                                }
                            },
                            expanded = searchState.searchBarActive,
                            onExpandedChange = { onSearchEvent(SearchEvent.SetSearchBarActive(it)) },
                            placeholder = { Text(stringResource(Res.string.search_dictionary)) },
                            leadingIcon = {
                                if (searchState.searchBarActive) {
                                    IconButton({ onSearchEvent(SearchEvent.SetSearchBarActive(false)) }) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "back")
                                    }
                                } else Icon(Icons.Default.Search, "Search")
                            },
                            trailingIcon = {
                                if (searchState.searchBarActive || searchState.searchTerm.isNotBlank()) {
                                    IconButton({ onSearchEvent(SearchEvent.ClearSearchBar) }) {
                                        Icon(Icons.Default.Clear, "clear")
                                    }
                                }
                            }
                        )
                    },
                    expanded = searchState.searchBarActive,
                    onExpandedChange = { onSearchEvent(SearchEvent.SetSearchBarActive(it)) },
                    tonalElevation = 50000.dp,
                    shadowElevation = 6.dp,
                    windowInsets = WindowInsets(0.dp)
                ) {
                    searchState.searchResults?.let { results ->
                        LazyColumn {
                            if (results.isEmpty()) {
                                item { Text("No suggestions") }
                            }

                            items(results) { entry ->
                                val word = entry.lexemeIPA
                                Text(
                                    text = word,
                                    modifier = Modifier
                                        .clickable {
                                            onSearchEvent(SearchEvent.SelectSuggestion(word))
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
