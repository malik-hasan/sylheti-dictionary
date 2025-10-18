package oats.mobile.sylhetidictionary.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import oats.mobile.sylhetidictionary.ui.components.SDNavigationDrawer
import oats.mobile.sylhetidictionary.ui.theme.SDTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(vm: AppViewModel = koinViewModel()) {
    val theme by vm.theme.collectAsStateWithLifecycle()
    val dynamicTheme by vm.dynamicTheme.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val selectedRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    SDProvider {
        SDTheme(
            dynamicTheme = dynamicTheme,
            darkTheme = theme.isDarkTheme()
        ) {
            Box(Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
            ) {
                SDNavigationDrawer(selectedRoute, navController::navigate) {
                    SDNavHost(navController)
                }
            }
        }
    }
}
