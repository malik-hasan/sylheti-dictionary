package ui.providers

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }
val LocalDrawerState = compositionLocalOf { DrawerState(DrawerValue.Closed) }

@Composable
fun SylhetiDictionaryProvider(content: @Composable () -> Unit) =
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalDrawerState provides DrawerState(DrawerValue.Closed)
    ) { content() }
