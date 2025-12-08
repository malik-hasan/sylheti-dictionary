package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import oats.mobile.sylhetidictionary.ui.app.LocalNavigationRailState
import oats.mobile.sylhetidictionary.ui.models.Route
import oats.mobile.sylhetidictionary.utility.isCompactWidth

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SDNavigationRail(
    currentDestination: NavDestination?,
    navigate: (Route) -> Unit,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    state: WideNavigationRailState = LocalNavigationRailState.current
) {
    if (windowAdaptiveInfo.isCompactWidth) {
        ModalWideNavigationRail(
            state = state,
            hideOnCollapse = true
        ) { NavigationRailItems(currentDestination, navigate) }
    } else WideNavigationRail(
        state = state
    ) { NavigationRailItems(currentDestination, navigate) }
}
