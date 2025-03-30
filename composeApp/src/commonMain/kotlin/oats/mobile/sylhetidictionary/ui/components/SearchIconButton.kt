package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import oats.mobile.sylhetidictionary.models.Route
import oats.mobile.sylhetidictionary.ui.app.LocalNavController

@Composable
fun SearchIconButton(
    modifier: Modifier = Modifier,
    navController: NavController = LocalNavController.current
) {
    IconButton(
        modifier = modifier,
        onClick = {
            with(navController) {
                getBackStackEntry(Route.Search).savedStateHandle[Route.Search.ACTIVATE_SEARCH_BAR_KEY] = true
                popBackStack(Route.Search, false)
            }
        }
    ) { Icon(Icons.Default.Search, "search") }
}
