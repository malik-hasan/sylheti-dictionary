package oats.mobile.sylhetidictionary.ui.screens.search.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import oats.mobile.sylhetidictionary.ui.components.DrawerIconButton
import oats.mobile.sylhetidictionary.ui.components.EntryCard
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.ScrollBar
import oats.mobile.sylhetidictionary.ui.components.SearchSettingsMenu
import oats.mobile.sylhetidictionary.ui.components.SearchSuggestion
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.asset_load_error
import sylhetidictionary.composeapp.generated.resources.history
import sylhetidictionary.composeapp.generated.resources.no_results
import sylhetidictionary.composeapp.generated.resources.search_dictionary
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.suggestion
import sylhetidictionary.composeapp.generated.resources.sylheti_dictionary
import sylhetidictionary.composeapp.generated.resources.tune

@Composable
fun SearchScreen(
    activateSearchBar: Boolean,
    vm: SearchViewModel = koinViewModel(),
    clearActivateSearchBar: () -> Unit
) = with(vm) {

    LaunchedEffect(activateSearchBar) {
        if (activateSearchBar) {
            vm.onSearchEvent(SearchEvent.SetSearchBarActive(true))
            clearActivateSearchBar()
        }
    }

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
    onSettingsEvent: (SearchSettingsEvent) -> Unit
) {
    SDScreen(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = { DrawerIconButton() },
                title = { Text(stringResource(Res.string.sylheti_dictionary)) },
                actions = {
                    Box {
                        IconButton({
                            onSearchEvent(SearchEvent.ToggleSettingsMenu(true))
                        }) {
                            Icon(
                                painter = painterResource(Res.drawable.tune),
                                contentDescription = stringResource(Res.string.settings)
                            )
                        }

                        SearchSettingsMenu(
                            expanded = searchState.menuExpanded,
                            onDismiss = { onSearchEvent(SearchEvent.ToggleSettingsMenu(false)) },
                            state = settingsState,
                            onEvent = onSettingsEvent
                        )
                    }
                }
            )
        }
    ) {
        if (assetLoaded == false) {
            Text(
                text = stringResource(Res.string.asset_load_error),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            Column {
                if (searchTerm.isNotBlank() && searchState.resultsLoading) {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                } else Spacer(Modifier.height(4.dp))

                Row {
                    val resultsState = rememberLazyListState()
                    var scrollingFromScrollBar by remember { mutableStateOf(false) }

                    Box(Modifier
                        .animateContentSize()
                        .weight(1f)
                    ) {
                        var previousFirstVisibleItemIndex by remember { mutableStateOf(0) }
                        var previousFirstVisibleItemScrollOffset by remember { mutableStateOf(Int.MAX_VALUE) }

                        val showSearchBar by remember {
                            derivedStateOf {
                                with(resultsState) {
                                    val isScrollingUp = firstVisibleItemIndex < previousFirstVisibleItemIndex ||
                                            (firstVisibleItemIndex == previousFirstVisibleItemIndex && firstVisibleItemScrollOffset < previousFirstVisibleItemScrollOffset)

                                    previousFirstVisibleItemIndex = firstVisibleItemIndex
                                    previousFirstVisibleItemScrollOffset = firstVisibleItemScrollOffset

                                    when {
                                        // always show if at top
                                        firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0 -> true

                                        // always hide if scrolling from scrollbar (mobile only)
                                        scrollingFromScrollBar -> {
                                            scrollingFromScrollBar = false
                                            false
                                        }

                                        // else show/hide based on isScrollingUp
                                        else -> isScrollingUp
                                    }
                                }
                            }
                        }

                        SideEffect {
                            resultsState.requestScrollToItem(
                                index = resultsState.firstVisibleItemIndex,
                                scrollOffset = resultsState.firstVisibleItemScrollOffset
                            )
                        }

                        LazyColumn(
                            state = resultsState,
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
                                if (entries.isEmpty() && searchTerm.isNotEmpty() && !resultsLoading) {
                                    item { Text(stringResource(Res.string.no_results)) }
                                    return@LazyColumn
                                }

                                items(
                                    items = entries,
                                    key = { it.entryId }
                                ) { entry ->
                                    EntryCard(entry) { value ->
                                        onSearchEvent(SearchEvent.Bookmark(entry.entryId, value))
                                    }
                                }
                            }
                        }

                        this@Column.AnimatedVisibility(
                            visible = showSearchBar,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            val searchBarPadding by animateDpAsState(if (searchState.searchBarActive) 0.dp else 16.dp)
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
                                                IconButton(
                                                    onClick = { onSearchEvent(SearchEvent.SetSearchBarActive(false)) }
                                                ) {
                                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "back")
                                                }
                                            } else Icon(Icons.Default.Search, "Search")
                                        },
                                        trailingIcon = {
                                            if (searchTerm.isNotBlank()) {
                                                IconButton({ onSearchEvent(SearchEvent.SelectSuggestion("")) }) {
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
                                val suggestionsState = rememberLazyListState()

                                SideEffect {
                                    suggestionsState.requestScrollToItem(
                                        index = suggestionsState.firstVisibleItemIndex,
                                        scrollOffset = suggestionsState.firstVisibleItemScrollOffset
                                    )
                                }

                                LazyColumn(state = suggestionsState) {
                                    items(searchState.recents) { recent ->
                                        SearchSuggestion(
                                            suggestion = recent,
                                            onClick = { onSearchEvent(SearchEvent.SelectSuggestion(recent.text)) }
                                        ) {
                                            Icon(
                                                painter = painterResource(Res.drawable.history),
                                                contentDescription = "Recent"
                                            )
                                        }
                                    }

                                    items(searchState.suggestions) { suggestion ->
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

                    val showScrollBar by remember(searchState) {
                        derivedStateOf {
                            (resultsState.canScrollForward || resultsState.canScrollBackward)
                                && !searchState.searchBarActive
                                && searchState.scrollCharIndexes.size > 4
                        }
                    }

                    AnimatedVisibility(
                        visible = showScrollBar,
                        enter = slideInHorizontally { it },
                        exit = slideOutHorizontally { it },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        ScrollBar(
                            lazyListState = resultsState,
                            scrollCharIndexes = searchState.scrollCharIndexes,
                            scrollingFromScrollBar = { scrollingFromScrollBar = true }
                        )
                    }
                }
            }
        }
    }
}
