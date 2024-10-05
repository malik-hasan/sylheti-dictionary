package di

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import models.Language
import org.koin.compose.viewmodel.koinViewModel
import ui.AppViewModel

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }
val LocalDrawerState = compositionLocalOf { DrawerState(DrawerValue.Closed) }
val LocalLanguage = compositionLocalOf { Language.EN }
val LocalDynamicTheme = compositionLocalOf { true }

@Composable
fun SDProvider(
    appViewModel: AppViewModel = koinViewModel(),
    content: @Composable () -> Unit
) {
    val language by appViewModel.language.collectAsStateWithLifecycle()
    val dynamicTheme by appViewModel.dynamicTheme.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalDrawerState provides DrawerState(DrawerValue.Closed),
        LocalLanguage provides language,
        LocalDynamicTheme provides dynamicTheme
    ) { content() }
}
