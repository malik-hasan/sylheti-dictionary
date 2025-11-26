package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.ui.app.LocalDrawerState
import oats.mobile.sylhetidictionary.ui.models.Route

@Composable
fun SDNavigationDrawerItem(
    label: String,
    route: Route,
    currentDestination: NavDestination?,
    navigate: (Route) -> Unit,
    drawerState: DrawerState = LocalDrawerState.current
) {
    val scope = rememberCoroutineScope()

    NavigationDrawerItem(
        label = { Text(label) },
        selected = currentDestination?.hasRoute(route::class) == true,
        onClick = {
            navigate(route)
            scope.launch {
                drawerState.close()
            }
        }
    )
}
