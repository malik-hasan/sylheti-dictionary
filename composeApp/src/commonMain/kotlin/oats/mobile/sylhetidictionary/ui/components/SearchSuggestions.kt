package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.ui.models.SDString
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchEvent
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchState
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.history
import sylhetidictionary.composeapp.generated.resources.suggestion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSuggestions(
    searchBarState: SearchBarState,
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    fun onSuggestionSelected(suggestion: SDString) = scope.launch {
        onSearchEvent(SearchEvent.SelectSuggestion(suggestion.text))
        searchBarState.animateToCollapsed()
    }

    LazyColumn {
        items(searchState.recents) { recent ->
            SearchSuggestion(
                suggestion = recent,
                onClick = { onSuggestionSelected(recent) },
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
                onClick = { onSuggestionSelected(suggestion) }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.suggestion),
                    contentDescription = "Suggestion"
                )
            }
        }
    }
}
