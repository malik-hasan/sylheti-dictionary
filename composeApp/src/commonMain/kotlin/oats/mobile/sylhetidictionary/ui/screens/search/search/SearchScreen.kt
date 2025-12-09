package oats.mobile.sylhetidictionary.ui.screens.search.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedDockedSearchBar
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.ui.components.EntryCard
import oats.mobile.sylhetidictionary.ui.components.NavigationRailIconButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.ScrollBar
import oats.mobile.sylhetidictionary.ui.components.SearchBarInputField
import oats.mobile.sylhetidictionary.ui.components.SearchSettingsMenu
import oats.mobile.sylhetidictionary.ui.components.SearchSuggestions
import oats.mobile.sylhetidictionary.ui.utils.isCompactWidth
import oats.mobile.sylhetidictionary.ui.utils.isExpanded
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.asset_load_error
import sylhetidictionary.composeapp.generated.resources.no_results
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.tune

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchScreen(
    activateSearchBar: Boolean,
    navigateToEntry: (entryId: String) -> Unit,
    assetLoaded: Boolean?,
    snackbarHostState: SnackbarHostState,
    searchInputState: TextFieldState,
    resultsListState: LazyListState,
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    settingsState: SearchSettingsState,
    onSettingsEvent: (SearchSettingsEvent) -> Unit,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val isCompactWindowWidth = windowAdaptiveInfo.isCompactWidth
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    SDScreen(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            val searchBarState = rememberSearchBarState()

            LaunchedEffect(Unit) {
                if (activateSearchBar) searchBarState.animateToExpanded()
            }

            LaunchedEffect(searchBarState.currentValue) {
                if (searchBarState.isExpanded) {
                    onSearchEvent(SearchEvent.UpdateLastSearchedTerm(searchInputState.text.toString()))
                } else searchInputState.setTextAndPlaceCursorAtEnd(searchState.lastSearchedTerm)
            }

            AppBarWithSearch(
                state = searchBarState,
                scrollBehavior = scrollBehavior,
                navigationIcon = { NavigationRailIconButton() },
                inputField = {
                    SearchBarInputField(
                        searchBarState = searchBarState,
                        searchInputState = searchInputState,
                        onSearchEvent = onSearchEvent
                    )
                },
                actions = {
                    Box {
                        IconButton({
                            onSearchEvent(SearchEvent.OpenSettingsMenu(true))
                        }) {
                            Icon(
                                painter = painterResource(Res.drawable.tune),
                                contentDescription = stringResource(Res.string.settings)
                            )
                        }

                        SearchSettingsMenu(
                            searchState = searchState,
                            onSearchEvent = onSearchEvent,
                            settingsState = settingsState,
                            onSettingsEvent = onSettingsEvent
                        )
                    }
                }
            )

            if (isCompactWindowWidth) {
                ExpandedFullScreenSearchBar(
                    state = searchBarState,
                    inputField = {
                        SearchBarInputField(
                            searchBarState = searchBarState,
                            searchInputState = searchInputState,
                            onSearchEvent = onSearchEvent
                        )
                    }
                ) { SearchSuggestions(searchBarState, searchState, onSearchEvent) }
            } else {
                ExpandedDockedSearchBar(
                    state = searchBarState,
                    inputField = {
                        SearchBarInputField(
                            searchBarState = searchBarState,
                            searchInputState = searchInputState,
                            onSearchEvent = onSearchEvent
                        )
                    }
                ) { SearchSuggestions(searchBarState, searchState, onSearchEvent) }
            }
        }
    ) { scaffoldPadding ->
        if (assetLoaded == false) {
            Text(
                text = stringResource(Res.string.asset_load_error),
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            Box(Modifier.padding(top = scaffoldPadding.calculateTopPadding())) {
                Row {
                    Box(Modifier
                        .animateContentSize()
                        .weight(1f)
                    ) {
                        LazyColumn(
                            state = resultsListState,
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp + scaffoldPadding.calculateBottomPadding()
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            with(searchState) {
                                if (entries.isEmpty() && searchInputState.text.isNotEmpty() && !resultsLoading) {
                                    item { Text(stringResource(Res.string.no_results)) }
                                    return@LazyColumn
                                }

                                items(
                                    items = entries,
                                    key = { it.entryId }
                                ) { entry ->
                                    EntryCard(
                                        entry = entry,
                                        navigateToEntry = navigateToEntry,
                                        featureBengaliDefinitions = searchState.featureBengaliDefinitions,
                                        setBookmark = { value ->
                                            onSearchEvent(SearchEvent.Bookmark(entry.entryId, value))
                                        }
                                    )
                                }
                            }
                        }
                    }

                    val showScrollBar by remember(searchState) {
                        derivedStateOf {
                            resultsListState.run { canScrollForward || canScrollBackward }
                                && searchState.scrollCharIndexes.run { isEmpty() || size > 4 }
                        }
                    }

                    if (showScrollBar) {
                        ScrollBar(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
                                .align(Alignment.CenterVertically),
                            lazyListState = resultsListState,
                            scrollCharIndexes = searchState.scrollCharIndexes
                        )
                    }
                }

                if (searchInputState.text.isNotBlank() && searchState.resultsLoading) {
                    LinearWavyProgressIndicator(Modifier.fillMaxWidth())
                }
            }
        }
    }
}
