package ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import ui.components.SDNavigationDrawer
import ui.theme.SDTheme

@Composable
fun App(vm: AppViewModel = koinViewModel()) {
    val theme by vm.theme.collectAsStateWithLifecycle()
    val dynamicTheme by vm.dynamicTheme.collectAsStateWithLifecycle()

    KoinContext {
        SDProvider {
            SDTheme(
                dynamicTheme = dynamicTheme,
                darkTheme = theme.isDarkTheme()
            ) {
                SDNavigationDrawer {
                    SDNavHost()
                }
            }
        }
    }
}
