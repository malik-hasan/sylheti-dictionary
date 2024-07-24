
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import org.koin.compose.KoinContext
import ui.components.NavigationDrawer
import ui.screens.search.SearchScreen
import ui.theme.SylhetiDictionaryTheme

@Composable
fun App() {
    KoinContext {
        SylhetiDictionaryTheme {
            NavigationDrawer {
                SearchScreen()
            }
        }
    }
}
