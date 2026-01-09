package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import oats.mobile.sylhetidictionary.BuildKonfig
import oats.mobile.sylhetidictionary.ui.app.LocalNavigationRailState
import oats.mobile.sylhetidictionary.ui.models.Route
import oats.mobile.sylhetidictionary.ui.utils.isCompactWidth
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.about
import sylhetidictionary.composeapp.generated.resources.debug
import sylhetidictionary.composeapp.generated.resources.info
import sylhetidictionary.composeapp.generated.resources.ipa
import sylhetidictionary.composeapp.generated.resources.ipa_help
import sylhetidictionary.composeapp.generated.resources.search
import sylhetidictionary.composeapp.generated.resources.settings

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SDNavigationRail(
    darkTheme: Boolean,
    currentDestination: NavDestination?,
    navigate: (Route) -> Unit,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    state: WideNavigationRailState = LocalNavigationRailState.current
) {
    val isCompactWindowWidth = windowAdaptiveInfo.isCompactWidth
    LaunchedEffect(isCompactWindowWidth) {
        if (!isCompactWindowWidth) state.collapse()
    }

    Row {
        ModalWideNavigationRail(
            state = state,
            hideOnCollapse = isCompactWindowWidth
        ) {
            DialogWindowStatusBarsEffect(darkTheme)

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SDNavigationRailItem(
                    label = stringResource(Res.string.search),
                    iconPainter = painterResource(Res.drawable.search),
                    route = Route.Search(),
                    currentDestination = currentDestination,
                    navigate = navigate
                )

                SDNavigationRailItem(
                    label = stringResource(Res.string.settings),
                    iconPainter = painterResource(Res.drawable.settings),
                    route = Route.Settings,
                    currentDestination = currentDestination,
                    navigate = navigate
                )

                SDNavigationRailItem(
                    label = stringResource(Res.string.ipa_help),
                    iconPainter = painterResource(Res.drawable.ipa),
                    route = Route.IpaHelp,
                    currentDestination = currentDestination,
                    navigate = navigate
                )

                SDNavigationRailItem(
                    label = stringResource(Res.string.about),
                    iconPainter = painterResource(Res.drawable.info),
                    route = Route.About,
                    currentDestination = currentDestination,
                    navigate = navigate
                )

                if (BuildKonfig.DEBUG) {
                    SDNavigationRailItem(
                        label = "Debug Menu",
                        iconPainter = painterResource(Res.drawable.debug),
                        route = Route.Debug,
                        currentDestination = currentDestination,
                        navigate = navigate
                    )
                }
            }
        }

        if (!isCompactWindowWidth) VerticalDivider(Modifier.fillMaxHeight())
    }
}
