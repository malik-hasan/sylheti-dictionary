package oats.mobile.sylhetidictionary.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import oats.mobile.sylhetidictionary.ui.components.SDNavigationDrawer
import oats.mobile.sylhetidictionary.ui.theme.SDTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    processTextSearchTerm: String? = null,
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
        ) {
            val navController = rememberNavController()
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination

            SDNavigationDrawer(
                currentDestination = currentDestination,
                navigate = { route ->
                    if (currentDestination?.hasRoute(route::class) != true) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    }
                }
            ) {
                SDNavHost(navController, processTextSearchTerm)
            }
        }
    }
}
