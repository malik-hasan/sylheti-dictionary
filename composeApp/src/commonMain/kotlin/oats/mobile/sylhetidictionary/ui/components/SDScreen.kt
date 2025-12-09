package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import oats.mobile.sylhetidictionary.ui.utils.isCompactWidth

@Composable
fun SDScreen(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) = Scaffold(
    modifier = modifier,
    topBar = topBar,
    snackbarHost = snackbarHost,
    content = content,
    contentWindowInsets = WindowInsets.safeDrawing.let {
        if (windowAdaptiveInfo.isCompactWidth) {
            it
        } else it.only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
    }
)
