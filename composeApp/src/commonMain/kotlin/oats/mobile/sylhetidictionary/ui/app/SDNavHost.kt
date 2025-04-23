package oats.mobile.sylhetidictionary.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import oats.mobile.sylhetidictionary.ui.models.Route
import oats.mobile.sylhetidictionary.ui.screens.AboutScreen
import oats.mobile.sylhetidictionary.ui.screens.IpaHelpScreen
import oats.mobile.sylhetidictionary.ui.screens.search.SearchNavHost
import oats.mobile.sylhetidictionary.ui.screens.search.SearchProvider
import oats.mobile.sylhetidictionary.ui.screens.settings.SettingsScreen
import oats.mobile.sylhetidictionary.ui.screens.settings.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SDNavHost(navController: NavHostController) {
    NavHost(navController, Route.Search) {
        composable<Route.Search> {
            SearchProvider {
                SearchNavHost()
            }
        }

        composable<Route.Settings> {
            with(koinViewModel<SettingsViewModel>()) {
                val state by state.collectAsStateWithLifecycle()

                SettingsScreen(state, ::onEvent)
            }
        }

        composable<Route.IpaHelp> {
            IpaHelpScreen()
        }

        composable<Route.About> {
            AboutScreen()
        }
    }
}
