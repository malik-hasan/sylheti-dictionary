package di

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
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
    vm: AppViewModel = koinViewModel(),
    content: @Composable () -> Unit
) {
    val language by vm.language.collectAsStateWithLifecycle()
    val dynamicTheme by vm.dynamicTheme.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        vm.refreshLanguage()
    }

    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalDrawerState provides DrawerState(DrawerValue.Closed),
        LocalLanguage provides language,
        LocalDynamicTheme provides dynamicTheme
    ) { content() }
}
