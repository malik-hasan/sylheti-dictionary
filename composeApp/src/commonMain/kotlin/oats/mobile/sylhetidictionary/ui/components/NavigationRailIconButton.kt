package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.ui.app.LocalNavigationRailState
import oats.mobile.sylhetidictionary.utility.isCompactWidth
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.menu

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavigationRailIconButton(
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    navigationRailState: WideNavigationRailState = LocalNavigationRailState.current
) = AnimatedVisibility(windowAdaptiveInfo.isCompactWidth) {
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch { navigationRailState.toggle() }
        }
    ) {
        Icon(
            painter = painterResource(Res.drawable.menu),
            contentDescription = "Menu"
        )
    }
}
