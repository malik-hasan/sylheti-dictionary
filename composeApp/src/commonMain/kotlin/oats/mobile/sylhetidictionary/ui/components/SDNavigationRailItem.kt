package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import oats.mobile.sylhetidictionary.ui.models.Route
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.search

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SDNavigationRailItem(
    label: String,
    route: Route,
    currentDestination: NavDestination?,
    railExpanded: Boolean,
    navigate: (Route) -> Unit,
) = WideNavigationRailItem(
    selected = currentDestination?.hasRoute(route::class) == true,
    onClick = { navigate(route) },
    icon = {
        Icon(
            painter = painterResource(Res.drawable.search),
            contentDescription = label
        )
    },
    label = { Text(label) },
    railExpanded = railExpanded
)
