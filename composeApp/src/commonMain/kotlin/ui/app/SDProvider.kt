package ui.app

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import models.settings.Language
import org.koin.compose.viewmodel.koinViewModel

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }
val LocalDrawerState = compositionLocalOf { DrawerState(DrawerValue.Closed) }
val LocalLanguage = staticCompositionLocalOf { Language.EN }
val LocalShowNagri = compositionLocalOf { false }

@Composable
fun SDProvider(
    vm: AppViewModel = koinViewModel(),
    content: @Composable () -> Unit
) {
    val language by vm.language.collectAsStateWithLifecycle()
    val showNagri by vm.showNagri.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        vm.refreshLanguage()
    }

    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalDrawerState provides DrawerState(DrawerValue.Closed),
        LocalLanguage provides language,
        LocalShowNagri provides showNagri
    ) { content() }
}
