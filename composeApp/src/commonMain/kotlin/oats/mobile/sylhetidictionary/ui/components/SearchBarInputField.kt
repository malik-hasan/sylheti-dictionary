package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchEvent
import oats.mobile.sylhetidictionary.ui.utils.isExpanded
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.clear
import sylhetidictionary.composeapp.generated.resources.search_sylheti_dictionary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarInputField(
    searchBarState: SearchBarState,
    searchInputState: TextFieldState,
    onSearchEvent: (SearchEvent) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
) = SearchBarDefaults.InputField(
    textFieldState = searchInputState,
    searchBarState = searchBarState,
    onSearch = {
        scope.launch {
            onSearchEvent(SearchEvent.Search)
            searchBarState.animateToCollapsed()
        }
    },
    placeholder = {
        Text(
            text = stringResource(Res.string.search_sylheti_dictionary),
            color = MaterialTheme.colorScheme.tertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    },
    leadingIcon = {
        if (searchBarState.isExpanded) {
            BackIconButton(
                onClick = {
                    scope.launch { searchBarState.animateToCollapsed() }
                }
            )
        } else SearchIcon()
    },
    trailingIcon = {
        if (searchInputState.text.isNotBlank()) {
            IconButton({ onSearchEvent(SearchEvent.ClearSearch(searchBarState.isExpanded)) }) {
                Icon(
                    painter = painterResource(Res.drawable.clear),
                    contentDescription = "Clear"
                )
            }
        }
    }
)
