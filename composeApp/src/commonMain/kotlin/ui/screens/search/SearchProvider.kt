package ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import ui.app.AppViewModel
import ui.app.LocalNavController

val LocalHighlightRegex = compositionLocalOf { Regex("") }
val LocalMappedIpaHighlightRegex = compositionLocalOf { Regex("") }

@Composable
fun SearchProvider(
    vm: AppViewModel = koinViewModel(),
    content: @Composable () -> Unit
) {
    val highlightRegex by vm.highlightRegex.collectAsStateWithLifecycle()
    val mappedIpaHighlightRegex by vm.mappedIpaHighlightRegex.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalHighlightRegex provides Regex(highlightRegex),
        LocalMappedIpaHighlightRegex provides Regex(mappedIpaHighlightRegex)
    ) { content() }
}
