package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import oats.mobile.sylhetidictionary.ui.app.LocalDrawerState
import oats.mobile.sylhetidictionary.ui.app.LocalNavController
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.models.Route

@Composable
fun SDNavigationDrawerItem(
    label: String,
    route: Route,
    navController: NavHostController = LocalNavController.current,
    drawerState: DrawerState = LocalDrawerState.current
) {
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationDrawerItem(
        label = { Text(label) },
        selected = currentBackStackEntry?.destination?.route == route::class.qualifiedName,
        onClick = {
            navController.navigate(route)
            scope.launch {
                drawerState.close()
            }
        }
    )
}
