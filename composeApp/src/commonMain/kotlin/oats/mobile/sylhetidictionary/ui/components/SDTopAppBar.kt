package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import oats.mobile.sylhetidictionary.utility.isCompactWidth

val SDTopAppBarWindowInsets
    @Composable get() = WindowInsets.safeDrawing.run {
        if (currentWindowAdaptiveInfo().isCompactWidth) {
            only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
        } else only(WindowInsetsSides.Top + WindowInsetsSides.End)
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SDTopAppBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) = TopAppBar(
    modifier = modifier,
    navigationIcon = navigationIcon,
    title = title,
    actions = actions,
    colors = colors,
    scrollBehavior = scrollBehavior,
    windowInsets = SDTopAppBarWindowInsets
)
