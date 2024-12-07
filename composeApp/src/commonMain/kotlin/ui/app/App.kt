package ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    val theme by vm.theme.collectAsStateWithLifecycle()
    val dynamicTheme by vm.dynamicTheme.collectAsStateWithLifecycle()

    KoinContext {
        SDProvider {
            SDTheme(
                dynamicTheme = dynamicTheme,
                darkTheme = theme.isDarkTheme()
            ) {
                Box(Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                ) {
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
}
