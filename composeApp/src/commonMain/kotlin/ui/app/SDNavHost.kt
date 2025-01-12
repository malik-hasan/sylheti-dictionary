package ui.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import models.Route
import ui.screens.AboutScreen
import ui.screens.IpaHelpScreen
import ui.screens.search.SearchNavHost
import ui.screens.search.SearchProvider
import ui.screens.settings.SettingsScreen

@Composable
fun SDNavHost(navController: NavHostController = LocalNavController.current) {
    NavHost(navController, Route.Search) {
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
