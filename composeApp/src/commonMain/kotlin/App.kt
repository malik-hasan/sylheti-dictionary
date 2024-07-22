
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import ui.components.NavigationDrawer
import ui.screens.search.SearchScreen
import ui.theme.SylhetiDictionaryTheme

@Composable
@Preview
fun App() {
    KoinContext {
        SylhetiDictionaryTheme {
            NavigationDrawer {
                SearchScreen()
            }
        }
    }
}
