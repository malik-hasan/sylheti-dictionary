@file:OptIn(ExperimentalSharedTransitionApi::class)

package oats.mobile.sylhetidictionary.ui.screens.search

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import oats.mobile.sylhetidictionary.ui.app.AppViewModel
import oats.mobile.sylhetidictionary.ui.app.LocalNavController

val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("No shared transition scope provided")
}

val LocalAnimatedContentScope = staticCompositionLocalOf<AnimatedContentScope> {
    error("No animated content scope provided")
}

val LocalHighlightRegex = compositionLocalOf { Regex("") }
val LocalMappedIpaHighlightRegex = compositionLocalOf { Regex("") }

@Composable
fun SearchProvider(
    vm: AppViewModel = koinViewModel(),
    content: @Composable () -> Unit
) {
    val highlightRegex by vm.highlightRegex.collectAsStateWithLifecycle()
    val mappedIpaHighlightRegex by vm.mappedIpaHighlightRegex.collectAsStateWithLifecycle()

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalNavController provides rememberNavController(),
            LocalSharedTransitionScope provides this,
            LocalHighlightRegex provides Regex(highlightRegex, RegexOption.IGNORE_CASE),
            LocalMappedIpaHighlightRegex provides Regex(mappedIpaHighlightRegex, RegexOption.IGNORE_CASE)
        ) { content() }
    }
}
