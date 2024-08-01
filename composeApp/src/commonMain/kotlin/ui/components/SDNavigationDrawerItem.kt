package ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import di.LocalDrawerState
import di.LocalNavController

@Composable
fun SDNavigationDrawerItem(
    label: String,
    route: Any,
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
            Logger.d("ROUTE ${navController.currentDestination?.route}, ${route::class.qualifiedName}")
            scope.launch {
                drawerState.close()
            }
        }
    )
}
