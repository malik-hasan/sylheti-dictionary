package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import oats.mobile.sylhetidictionary.ui.models.Route
import oats.mobile.sylhetidictionary.utility.debugBuild
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.about
import sylhetidictionary.composeapp.generated.resources.debug
import sylhetidictionary.composeapp.generated.resources.info
import sylhetidictionary.composeapp.generated.resources.ipa_help
import sylhetidictionary.composeapp.generated.resources.search
import sylhetidictionary.composeapp.generated.resources.settings

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavigationRailItems(
    currentDestination: NavDestination?,
    navigate: (Route) -> Unit
) = Column(Modifier
    .fillMaxHeight()
    .verticalScroll(rememberScrollState())
) {
    DrawerIconButton()

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
        iconPainter = painterResource(Res.drawable.search),
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

    if (debugBuild) {
        SDNavigationRailItem(
            label = "Debug Menu",
            iconPainter = painterResource(Res.drawable.debug),
            route = Route.Debug,
            currentDestination = currentDestination,
            navigate = navigate
        )
    }

    Spacer(Modifier.weight(1f))

    Text(
        text = "Powered by Oats Mobile",
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}
