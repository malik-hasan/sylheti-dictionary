package oats.mobile.sylhetidictionary.ui.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import oats.mobile.sylhetidictionary.models.Route
import oats.mobile.sylhetidictionary.ui.screens.AboutScreen
import oats.mobile.sylhetidictionary.ui.screens.IpaHelpScreen
import oats.mobile.sylhetidictionary.ui.screens.search.SearchNavHost
import oats.mobile.sylhetidictionary.ui.screens.search.SearchProvider
import oats.mobile.sylhetidictionary.ui.screens.settings.SettingsScreen

@Composable
fun SDNavHost(navController: NavHostController = LocalNavController.current) {
    NavHost(navController, Route.Search()) {
        composable<Route.Search> {
            SearchProvider {
                SearchNavHost()
            }
        }

        composable<Route.Settings> {
            SettingsScreen()
        }

        composable<Route.IpaHelp> {
            IpaHelpScreen()
        }

        composable<Route.About> {
            AboutScreen()
        }
    }
}
