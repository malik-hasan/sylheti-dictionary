
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import di.LocalNavController
import di.SDProvider
import org.koin.compose.KoinContext
import org.koin.core.annotation.KoinExperimentalAPI
import ui.components.SDNavigationDrawer
import ui.screens.search.SearchRoute
import ui.screens.search.SearchScreen
import ui.screens.settings.SettingsRoute
import ui.screens.settings.SettingsScreen
import ui.theme.SylhetiDictionaryTheme

@Composable
fun App() {
    KoinContext {
        SDProvider {
            SylhetiDictionaryTheme {
                SDNavigationDrawer {
                    NavHost(LocalNavController.current, SearchRoute) {
                        composable<SearchRoute> {
                            SearchScreen()
                        }
                        composable<SettingsRoute> {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}
