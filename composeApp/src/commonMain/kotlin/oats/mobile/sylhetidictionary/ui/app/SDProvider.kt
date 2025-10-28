package oats.mobile.sylhetidictionary.ui.app

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
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Language
import org.koin.compose.viewmodel.koinViewModel

val LocalDrawerState = compositionLocalOf { DrawerState(DrawerValue.Closed) }
val LocalLanguage = staticCompositionLocalOf { Language.EN } // static forces redraw of entire app so all string resources are refreshed

@Composable
fun SDProvider(
    vm: AppViewModel = koinViewModel(),
    content: @Composable () -> Unit
) {
    val language by vm.language.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        vm.refreshLanguage()
    }

    CompositionLocalProvider(
        LocalDrawerState provides DrawerState(DrawerValue.Closed),
        LocalLanguage provides language
    ) { content() }
}
