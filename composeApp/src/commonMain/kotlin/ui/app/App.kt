package ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import models.Route
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import ui.components.SDNavigationDrawer
import ui.screens.search.SearchScreen
import ui.screens.settings.SettingsScreen
import ui.theme.SDTheme

@Composable
fun App(vm: AppViewModel = koinViewModel()) {
    val dynamicTheme by vm.dynamicTheme.collectAsStateWithLifecycle()

    KoinContext {
        SDProvider {
            SDTheme(dynamicTheme) {
                SDNavigationDrawer {
                    NavHost(LocalNavController.current, Route.Search) {
                        composable<Route.Search> {
                            SearchScreen()
                        }
                        composable<Route.Settings> {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}
