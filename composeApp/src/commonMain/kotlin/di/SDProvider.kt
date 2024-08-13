package di

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import models.Locale
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.AppViewModel

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }
val LocalDrawerState = compositionLocalOf { DrawerState(DrawerValue.Closed) }
val LocalLocalization = staticCompositionLocalOf { Locale.English }

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SDProvider(
    appViewModel: AppViewModel = koinViewModel(),
    content: @Composable () -> Unit
) {
    val locale by appViewModel.locale.collectAsStateForPlatform()

    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalDrawerState provides DrawerState(DrawerValue.Closed),
        LocalLocalization provides locale
    ) { content() }
}
