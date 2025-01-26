package oats.mobile.sylhetidictionary.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import oats.mobile.sylhetidictionary.models.Route
import oats.mobile.sylhetidictionary.ui.app.LocalNavController
import oats.mobile.sylhetidictionary.ui.screens.search.entry.EntryScreen
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchScreen

@Composable
fun SearchNavHost(navController: NavHostController = LocalNavController.current) {
    NavHost(navController, Route.Search) {
        composable<Route.Search> {
            CompositionLocalProvider(LocalAnimatedContentScope provides this) {
                SearchScreen()
            }
        }

        composable<Route.Entry> {
            CompositionLocalProvider(LocalAnimatedContentScope provides this) {
                EntryScreen(it.toRoute<Route.Entry>().entryId)
            }
        }
    }
}
