package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import oats.mobile.sylhetidictionary.ui.models.Route
import oats.mobile.sylhetidictionary.utility.debugBuild
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.about
import sylhetidictionary.composeapp.generated.resources.ipa_help
import sylhetidictionary.composeapp.generated.resources.search
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.sylheti_dictionary

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SDNavigationRail(
    currentDestination: NavDestination?,
    navigate: (Route) -> Unit
) {
    val state = rememberWideNavigationRailState()

    ModalWideNavigationRail(
        state = state,
        hideOnCollapse = true,
        header = {
            Text(
                text = stringResource(Res.string.sylheti_dictionary),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    ) {
        Column(Modifier
//            .safeDrawingPadding()
//            .padding(8.dp)
//            .fillMaxHeight()
//            .verticalScroll(rememberScrollState())
        ) {
            val railExpanded = state.currentValue == WideNavigationRailValue.Expanded

            SDNavigationRailItem(stringResource(Res.string.search), Route.Search(), currentDestination, railExpanded, navigate)

            SDNavigationRailItem(stringResource(Res.string.settings), Route.Settings, currentDestination, railExpanded, navigate)

            SDNavigationRailItem(stringResource(Res.string.ipa_help), Route.IpaHelp, currentDestination, railExpanded, navigate)

            SDNavigationRailItem(stringResource(Res.string.about), Route.About, currentDestination, railExpanded, navigate)

            if (debugBuild) {
                SDNavigationRailItem("Debug Menu", Route.Debug, currentDestination, railExpanded, navigate)
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "Powered by Oats Mobile",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
            )
        }
    }
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            BackHandler(drawerState.isOpen) {
//                scope.launch {
//                    drawerState.close()
//                }
//            }
//
//            ModalDrawerSheet {
//                Column(Modifier
//                    .safeDrawingPadding()
//                    .padding(8.dp)
//                    .fillMaxHeight()
//                    .verticalScroll(rememberScrollState())
//                ) {
//                    Text(
//                        text = stringResource(Res.string.sylheti_dictionary),
//                        style = MaterialTheme.typography.headlineSmall,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 8.dp),
//                        textAlign = TextAlign.Center
//                    )
//
//                    SDNavigationRailItem(stringResource(Res.string.search), Route.Search(), selectedRoute, navigate)
//
//                    SDNavigationRailItem(stringResource(Res.string.settings), Route.Settings, selectedRoute, navigate)
//
//                    SDNavigationRailItem(stringResource(Res.string.ipa_help), Route.IpaHelp, selectedRoute, navigate)
//
//                    SDNavigationRailItem(stringResource(Res.string.about), Route.About, selectedRoute, navigate)
//
//                    if (debugBuild) {
//                        SDNavigationRailItem("Debug Menu", Route.Debug, selectedRoute, navigate)
//                    }
//
//                    Spacer(Modifier.weight(1f))
//
//                    Text(
//                        text = "Powered by Oats Mobile",
//                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
//                    )
//                }
//            }
//        },
//        content = content
//    )
}
