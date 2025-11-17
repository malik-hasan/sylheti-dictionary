package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.runtime.Composable
import oats.mobile.sylhetidictionary.ui.models.Route
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.search

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SDNavigationRailItem(
    label: String,
    route: Route,
    selectedRoute: String?,
    railExpanded: Boolean,
    navigate: (Route) -> Unit,
) = WideNavigationRailItem(
    selected = route::class.qualifiedName?.let { route ->
        selectedRoute?.startsWith(route)
    } == true,
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
