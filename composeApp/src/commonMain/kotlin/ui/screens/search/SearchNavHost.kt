package ui.screens.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import models.Route
import ui.app.LocalNavController
import ui.screens.search.entry.EntryScreen
import ui.screens.search.search.SearchScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchNavHost(navController: NavHostController = LocalNavController.current) {
    SharedTransitionLayout {
        NavHost(navController, Route.Search) {
            composable<Route.Search> {
                SearchScreen()
            }

            composable<Route.Entry> {
                val args = it.toRoute<Route.Entry>()
                EntryScreen(args.entryId)
            }
        }
    }
}
