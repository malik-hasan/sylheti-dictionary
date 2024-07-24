package ui.screens.search

import Language
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import kotlinx.coroutines.launch
import models.search.settings.LatinSearchLanguage
import models.search.settings.SearchPosition
import models.search.settings.SearchScript
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import setLanguage
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.sylheti_dictionary
import sylhetidictionary.composeapp.generated.resources.tune
import ui.components.EntryCard
import ui.components.LocalDrawerState
import ui.utils.collectAsStateForPlatform
import ui.utils.ifTrue
import ui.utils.isScrollingUp

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

    var dstate by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val scope = rememberCoroutineScope()
            val drawerState = LocalDrawerState.current
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(stringResource(Res.string.sylheti_dictionary))
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
                            Icon(painterResource(Res.drawable.tune), "settings")
                        }

                        DropdownMenu(
                            expanded = dstate,
                            onDismissRequest = { dstate = !dstate }
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Part of word to search")
                                MultiChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Min)) {
                                    val searchPositions = SearchPosition.entries
                                    searchPositions.forEachIndexed { index, searchPosition ->
                                        SegmentedButton(
                                            modifier = Modifier.fillMaxHeight(),
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
                                SingleChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Min)) {
                                    val searchScripts = SearchScript.entries
                                    searchScripts.forEachIndexed { index, searchScript ->
                                        SegmentedButton(
                                            modifier = Modifier.fillMaxHeight(),
                                            selected = false,
                                            onClick = {},
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = index,
                                                count = searchScripts.size
                                            )
                                        ) {
                                            Text(
                                                searchScript.toString(),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                Text("Search language")
                                MultiChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Min)) {
                                    val searchLanguages = LatinSearchLanguage.entries
                                    searchLanguages.forEachIndexed { index, searchLanguage ->
                                        SegmentedButton(
                                            modifier = Modifier.fillMaxHeight(),
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val searchResults = state.searchResults
                val bookmarks = state.bookmarks

                item {
                    Button(onClick = {
                        scope.launch {
                            setLanguage(Language.English.code)
                        }
                    }) {
                        Text("English")
                    }
                    Button(onClick = {
                        scope.launch {
                            setLanguage(Language.Bengali.code)
                        }
                    }) {
                        Text("Bengali")
                    }
                }

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
