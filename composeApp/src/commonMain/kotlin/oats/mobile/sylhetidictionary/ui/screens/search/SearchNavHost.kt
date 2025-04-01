package oats.mobile.sylhetidictionary.ui.screens.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import oats.mobile.sylhetidictionary.models.Route
import oats.mobile.sylhetidictionary.ui.screens.search.entry.EntryScreen
import oats.mobile.sylhetidictionary.ui.screens.search.entry.EntryViewModel
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchScreen
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchNavHost() {
    val navController = rememberNavController()

    fun navigateToEntry(entryId: String) {
        navController.navigate(Route.Entry(entryId))
    }

    NavHost(navController, Route.Search) {
        composable<Route.Search> {
            val activateSearchBar = it.savedStateHandle.remove<Unit>(Route.Search.ACTIVATE_SEARCH_BAR_KEY) != null

            CompositionLocalProvider(LocalAnimatedContentScope provides this) {
                with(koinViewModel<SearchViewModel>()) {
                    val assetLoaded by assetLoaded.collectAsStateWithLifecycle()
                    val searchState by searchState.collectAsStateWithLifecycle()
                    val settingsState by settingsState.collectAsStateWithLifecycle()

                    SearchScreen(
                        activateSearchBar = activateSearchBar,
                        navigateToEntry = ::navigateToEntry,
                        assetLoaded = assetLoaded,
                        snackbarHostState = snackbarHostState,
                        searchTerm = searchTerm,
                        searchState = searchState,
                        onSearchEvent = ::onSearchEvent,
                        settingsState = settingsState,
                        onSettingsEvent = ::onSettingsEvent
                    )
                }
            }
        }

        composable<Route.Entry> {
            CompositionLocalProvider(LocalAnimatedContentScope provides this) {
                with(koinViewModel<EntryViewModel> {
                    parametersOf(it.toRoute<Route.Entry>().entryId)
                }) {
                    val state by state.collectAsStateWithLifecycle()

                    EntryScreen(
                        state = state,
                        onEvent = ::onEvent,
                        navigateUp = navController::navigateUp,
                        popToSearchBar = {
                            with(navController) {
                                getBackStackEntry(Route.Search).savedStateHandle[Route.Search.ACTIVATE_SEARCH_BAR_KEY] = Unit
                                popBackStack(Route.Search, false)
                            }
                        },
                        navigateToEntry = ::navigateToEntry
                    )
                }
            }
        }
    }
}
