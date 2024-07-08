import androidx.compose.runtime.Composable
import ui.interfaces.NavigationDrawer
import ui.screens.search.SearchScreen
import ui.theme.SylhetiDictionaryTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SylhetiDictionaryTheme {
        NavigationDrawer {
            SearchScreen()
        }
    }
}
