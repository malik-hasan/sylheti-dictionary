package ui.screens.search.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.history
import sylhetidictionary.composeapp.generated.resources.search_dictionary
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.suggestion
import sylhetidictionary.composeapp.generated.resources.sylheti_dictionary
import sylhetidictionary.composeapp.generated.resources.tune
import ui.components.DrawerIconButton
import ui.components.EntryCard
import ui.components.SDScreen
import ui.components.SearchSettingsMenu
import ui.components.SearchSuggestion
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.utils.SDString
import ui.utils.rememberIsScrollingUp

@Composable
fun SearchScreen(
    vm: SearchViewModel = koinViewModel()
) = with(vm) {
    val assetLoaded by assetLoaded.collectAsStateWithLifecycle()
    val searchState by searchState.collectAsStateWithLifecycle()
    val settingsState by settingsState.collectAsStateWithLifecycle()

    SearchScreen(
        assetLoaded = assetLoaded,
        snackbarHostState = snackbarHostState,
        searchTerm = searchTerm,
        searchState = searchState,
        onSearchEvent = ::onSearchEvent,
        settingsState = settingsState,
        onSettingsEvent = ::onSettingsEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreen(
    assetLoaded: Boolean?,
    snackbarHostState: SnackbarHostState,
    searchTerm: String,
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    settingsState: SearchSettingsState,
    onSettingsEvent: (SearchSettingsEvent) -> Unit,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    SDScreen(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = { DrawerIconButton() },
                title = { Text(stringResource(Res.string.sylheti_dictionary)) },
                actions = {
                    Box {
                        IconButton(
                            onClick = { onSettingsEvent(SearchSettingsEvent.ToggleSettingsMenu(true)) }
                        ) {
                            Icon(painterResource(Res.drawable.tune), stringResource(Res.string.settings))
                        }

                        SearchSettingsMenu(settingsState, onSettingsEvent)
                    }
                }
            )
        }
    ) {

        val listState = rememberLazyListState()
        val isScrollingUp by listState.rememberIsScrollingUp()

        if (assetLoaded == false) {
            Text(
                text = "There was an error loading the dictionary data. Try restarting the app, or report a bug if the problem persists.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 72.dp,
                    bottom = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                with(searchState) {
                    searchResults?.ifEmpty {
                        item { Text("No results") }
                        return@LazyColumn
                    }

                    items(
                        items = entryToBookmark.toList(),
                        key = { (entry, _) -> entry.entryId }
                    ) { (entry, isBookmark) ->
                        EntryCard(entry, isBookmark) {
                            onSearchEvent(SearchEvent.Bookmark(entry.entryId, !isBookmark))
                        }
                    }
                }
            }

            val searchBarPadding by animateDpAsState(if (searchState.searchBarActive) 0.dp else 16.dp)

            AnimatedVisibility(
                visible = isScrollingUp,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                SearchBar(
                    modifier = Modifier
                        .padding(horizontal = searchBarPadding)
                        .fillMaxWidth(),
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchTerm,
                            onQueryChange = { onSearchEvent(SearchEvent.UpdateSearchTerm(it)) },
                            onSearch = { onSearchEvent(SearchEvent.Search) },
                            expanded = searchState.searchBarActive,
                            onExpandedChange = { onSearchEvent(SearchEvent.SetSearchBarActive(it)) },
                            placeholder = {
                                Text(
                                    text = stringResource(Res.string.search_dictionary),
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            },
                            leadingIcon = {
                                if (searchState.searchBarActive) {
                                    IconButton({
                                        onSearchEvent(
                                            SearchEvent.SetSearchBarActive(
                                                false
                                            )
                                        )
                                    }) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "back")
                                    }
                                } else Icon(Icons.Default.Search, "Search")
                            },
                            trailingIcon = {
                                if (searchTerm.isNotBlank()) {
                                    IconButton({ onSearchEvent(SearchEvent.UpdateSearchTerm("")) }) {
                                        Icon(Icons.Default.Clear, "Clear")
                                    }
                                }
                            }
                        )
                    },
                    expanded = searchState.searchBarActive,
                    onExpandedChange = { onSearchEvent(SearchEvent.SetSearchBarActive(it)) },
                    tonalElevation = 50000.dp,
                    shadowElevation = 6.dp,
                    windowInsets = WindowInsets(0)
                ) {
                    LazyColumn {
                        with(searchState) {
                            items(recents) { recent ->
                                SearchSuggestion(
                                    suggestion = SDString(recent, mappedIpaHighlightRegex, detectedSearchScript),
                                    onClick = { onSearchEvent(SearchEvent.SelectSuggestion(recent)) }
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.history),
                                        contentDescription = "Recent"
                                    )
                                }
                            }

                            suggestions?.let { suggestions ->
                                items(suggestions) { suggestion ->
                                    SearchSuggestion(
                                        suggestion = suggestion,
                                        onClick = { onSearchEvent(SearchEvent.SelectSuggestion(suggestion.text)) }
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.suggestion),
                                            contentDescription = "Suggestion"
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
