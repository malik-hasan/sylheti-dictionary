package oats.mobile.sylhetidictionary.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import oats.mobile.sylhetidictionary.ui.components.SDNavigationDrawer
import oats.mobile.sylhetidictionary.ui.theme.SDTheme
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

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
