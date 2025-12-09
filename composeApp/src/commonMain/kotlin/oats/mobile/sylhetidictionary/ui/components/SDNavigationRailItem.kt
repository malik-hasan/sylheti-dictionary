package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.ui.app.LocalNavigationRailState
import oats.mobile.sylhetidictionary.ui.models.Route
import oats.mobile.sylhetidictionary.ui.utils.isCompactWidth

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SDNavigationRailItem(
    label: String,
    iconPainter: Painter,
    route: Route,
    currentDestination: NavDestination?,
    navigate: (Route) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    navigationRailState: WideNavigationRailState = LocalNavigationRailState.current
) = WideNavigationRailItem(
    selected = currentDestination?.hasRoute(route::class) == true,
    onClick = {
        scope.launch {
            navigate(route)
            if (windowAdaptiveInfo.isCompactWidth) {
                navigationRailState.collapse()
            }
        }
    },
    icon = { Icon(iconPainter, label) },
    label = { Text(label) },
    railExpanded = navigationRailState.currentValue == WideNavigationRailValue.Expanded
)
