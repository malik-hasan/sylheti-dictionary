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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import di.collectAsStateForPlatform
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.sylheti_dictionary
import sylhetidictionary.composeapp.generated.resources.tune
import ui.components.EntryCard
import ui.components.SearchSettingsMenu
import ui.components.SylhetiDictionaryTopBar
import ui.utils.ifTrue
import ui.utils.isScrollingUp

@Serializable
object SearchRoute

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SearchScreen(vm: SearchViewModel = koinViewModel()) {
    val state by vm.state.collectAsStateForPlatform()

    SearchScreen(state, vm::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isSettingsMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SylhetiDictionaryTopBar(stringResource(Res.string.sylheti_dictionary), scrollBehavior) {
                Box {
                    IconButton(onClick = { isSettingsMenuOpen = true }) {
                        Icon(
                            painterResource(Res.drawable.tune),
                            stringResource(Res.string.settings)
                        )
                    }

                    SearchSettingsMenu(isSettingsMenuOpen) {
                        isSettingsMenuOpen = !isSettingsMenuOpen
                    }
                }
            }
        }
    ) { scaffoldPadding ->
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        if (!state.assetLoaded) {
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
                val searchResults = state.searchResults
                val bookmarks = state.bookmarks

                val items = searchResults?.let {
                    searchResults.ifEmpty {
                        item { Text("No results") }
                        return@LazyColumn
                    }
                } ?: bookmarks.ifEmpty { return@LazyColumn }

                items(items) { entry ->
                    EntryCard(entry) { entryId, isFavorite ->
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

                            items(results) { entry ->
                                val word = entry.lexemeIPA
                                Text(
                                    text = word,
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(
                                                SearchEvent.SelectSuggestion(word)
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
