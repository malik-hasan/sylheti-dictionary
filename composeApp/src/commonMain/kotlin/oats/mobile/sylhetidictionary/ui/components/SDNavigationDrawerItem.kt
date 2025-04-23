package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.ui.models.Route
import oats.mobile.sylhetidictionary.ui.app.LocalDrawerState

@Composable
fun SDNavigationDrawerItem(
    label: String,
    route: Route,
    selectedRoute: String?,
    navigate: (Route) -> Unit,
    drawerState: DrawerState = LocalDrawerState.current
) {
    val scope = rememberCoroutineScope()

    NavigationDrawerItem(
        label = { Text(label) },
        selected = selectedRoute == route::class.qualifiedName,
        onClick = {
            navigate(route)
            scope.launch {
                drawerState.close()
            }
        }
    )
}
