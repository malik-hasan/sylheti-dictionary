package oats.mobile.sylhetidictionary.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import oats.mobile.sylhetidictionary.ui.components.SDNavigationRail
import oats.mobile.sylhetidictionary.ui.models.Route
import oats.mobile.sylhetidictionary.ui.theme.SDTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    processTextSearchTerm: String? = null,
    focusManager: FocusManager = LocalFocusManager.current,
    vm: AppViewModel = koinViewModel()
) = SDProvider {
    val theme by vm.theme.collectAsStateWithLifecycle()
    val dynamicTheme by vm.dynamicTheme.collectAsStateWithLifecycle()

    SDTheme(
        dynamicTheme = dynamicTheme,
        darkTheme = theme.isDarkTheme()
    ) {
        Box(Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(null, null) {
                focusManager.clearFocus()
            }
        ) {
            val navController = rememberNavController()
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination

            SDNavigationRail(
                currentDestination = currentDestination,
                navigate = { route ->
                    if (currentDestination?.hasRoute(route::class) != true) {
                        navController.navigate(route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId)
                        }
                    } else if (route is Route.Search) {
                        navController.navigate(route) {
                            popUpTo(route) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
            SDNavHost(navController, processTextSearchTerm)
        }
    }
}
