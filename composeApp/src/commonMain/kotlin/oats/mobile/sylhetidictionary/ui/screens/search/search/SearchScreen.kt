package oats.mobile.sylhetidictionary.ui.screens.search.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import oats.mobile.sylhetidictionary.models.displayIPA
import oats.mobile.sylhetidictionary.ui.components.DrawerIconButton
import oats.mobile.sylhetidictionary.ui.components.EntryCard
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SearchSettingsMenu
import oats.mobile.sylhetidictionary.ui.components.SearchSuggestion
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily
import oats.mobile.sylhetidictionary.ui.utils.rememberIsScrollingUp
import oats.mobile.sylhetidictionary.utility.UnicodeUtility
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
import kotlin.collections.set

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
                    var touchedItem by remember { mutableStateOf<Char?>(null) }

                    Box(Modifier.weight(1f)) {
                        val resultsState = rememberLazyListState()
                        val isScrollingUp by resultsState.rememberIsScrollingUp()

                        SideEffect {
                            resultsState.requestScrollToItem(
                                index = resultsState.firstVisibleItemIndex,
                                scrollOffset = resultsState.firstVisibleItemScrollOffset
                            )
                        }

                        LaunchedEffect(touchedItem) {
                            touchedItem?.let { char ->
                                val touchedItemIndex = UnicodeUtility.SYLHETI_IPA_CHARS[char] ?: 0

                                val itemIndex = searchState.entries.indexOfFirst {
                                    (UnicodeUtility.SYLHETI_IPA_CHARS[it.displayIPA.first()] ?: 0) >= touchedItemIndex
                                }.takeUnless { it < 0 } ?: searchState.entries.lastIndex.takeUnless { it < 0 }

                                itemIndex?.let {
                                    resultsState.scrollToItem(it)
                                }
                            }
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
                            visible = isScrollingUp,
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

                    val itemCoordinates = remember { mutableStateMapOf<Char, LayoutCoordinates>() }
                    var backgroundColor by remember { mutableStateOf(Color.Unspecified) }
                    val surfaceContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    var dragPosition by remember { mutableStateOf<Offset?>(null) }

                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .widthIn(24.dp)
                            .background(backgroundColor)
                            .draggable(
                                state = rememberDraggableState { delta ->
                                    dragPosition?.let {
                                        dragPosition = it.copy(y = it.y + delta)
                                        touchedItem = itemCoordinates.entries.find { (_, coordinates) ->
                                            coordinates.isAttached && coordinates.boundsInParent().contains(dragPosition!!)
                                        }?.key
                                    }
                                },
                                startDragImmediately = true,
                                orientation = Orientation.Vertical,
                                onDragStarted = { startedPosition ->
                                    backgroundColor = surfaceContainerColor
                                    dragPosition = startedPosition
                                    touchedItem = itemCoordinates.entries.find { (_, coordinates) ->
                                        coordinates.isAttached && coordinates.boundsInParent().contains(startedPosition)
                                    }?.key
                                },
                                onDragStopped = {
                                    delay(700)
                                    backgroundColor = Color.Unspecified
                                    dragPosition = null
                                    touchedItem = null
                                }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val labelLarge = MaterialTheme.typography.labelLarge

                        UnicodeUtility.SYLHETI_IPA_CHARS.keys.toMutableList()
                            .apply { add(0, '-') }
                            .filter { it !in UnicodeUtility.NON_INITIAL_CHARS }
                            .forEach { char ->
                                var readyToDraw by remember { mutableStateOf(false) }
                                var textStyle by remember { mutableStateOf(labelLarge) }
    //                            val interactionSource = remember { MutableInteractionSource() }
    //                            val isHovered by interactionSource.collectIsHoveredAsState()
    //
    //                            LaunchedEffect(isHovered) {
    //                                touchedItem = if (isHovered) char else null
    //                            }

                                Text(
                                    text = char.toString(),
                                    textAlign = TextAlign.Center,
                                    style = textStyle,
                                    softWrap = false,
                                    fontFamily = latinDisplayFontFamily,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .drawWithContent {
                                            if (readyToDraw) drawContent()
                                        }.onGloballyPositioned { coordinates ->
                                            itemCoordinates[char] = coordinates
                                        },//.hoverable(interactionSource),
                                    onTextLayout = { textLayoutResult ->
                                        if (textLayoutResult.didOverflowHeight) {
                                            textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.95f)
                                        } else readyToDraw = true
                                    }
                                )
                            }

                            itemCoordinates[touchedItem]?.let { coordinates ->
                                val indicatorOffset by animateIntOffsetAsState(
                                    targetValue = IntOffset(0, coordinates.boundsInParent().top.toInt()),
                                    animationSpec = spring(stiffness = Spring.StiffnessHigh, visibilityThreshold = IntOffset.VisibilityThreshold)
                                )

                                Popup(offset = indicatorOffset) {
                                    Box(
                                        modifier = Modifier
                                            .offset(12.dp)
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.tertiary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = touchedItem.toString(),
                                            color = MaterialTheme.colorScheme.onTertiary,
                                            fontFamily = latinDisplayFontFamily
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
