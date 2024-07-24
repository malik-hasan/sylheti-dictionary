
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import co.touchlab.kermit.Logger
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.components.NavigationDrawer
import ui.screens.search.SearchScreen
import ui.theme.SylhetiDictionaryTheme

val LocalLocalization = staticCompositionLocalOf { Language.English.code }

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App(vm: MainViewModel = koinViewModel()) {
    Logger.d("APP RECOMPOSED")
    KoinContext {
        SylhetiDictionaryTheme {
//            val language by vm.language.collectAsStateForPlatform()
//            CompositionLocalProvider(LocalLocalization provides language) {
                NavigationDrawer {
                    SearchScreen()
                }
//            }
        }
    }
}
