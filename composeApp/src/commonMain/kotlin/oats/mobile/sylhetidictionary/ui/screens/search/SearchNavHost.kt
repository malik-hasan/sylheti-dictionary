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
import oats.mobile.sylhetidictionary.ui.screens.search.entry.EntryViewModel
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SearchNavHost(navController: NavHostController = LocalNavController.current) {
    NavHost(navController, Route.Search) {
        composable<Route.Search> {
            val activateSearchBar = it.savedStateHandle.remove(Route.Search.ACTIVATE_SEARCH_BAR_KEY) ?: false

            CompositionLocalProvider(LocalAnimatedContentScope provides this) {
                SearchScreen(activateSearchBar)
            }
        }

        composable<Route.Entry> {
            val vm: EntryViewModel = koinViewModel {
                parametersOf(it.toRoute<Route.Entry>().entryId)
            }

            CompositionLocalProvider(LocalAnimatedContentScope provides this) {
                EntryScreen(vm)
            }
        }
    }
}
