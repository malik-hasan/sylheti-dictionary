package ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import models.Route
import ui.app.LocalNavController

@Composable
fun SearchIconButton(
    modifier: Modifier = Modifier,
    navController: NavController = LocalNavController.current
) {
    IconButton(
        modifier = modifier,
        onClick = { navController.popBackStack(Route.Search, false) }
    ) { Icon(Icons.Default.Search, "search") }
}
