package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.ui.app.LocalNavigationRailState
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.menu

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DrawerIconButton(navigationRailState: WideNavigationRailState = LocalNavigationRailState.current) {
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                navigationRailState.toggle()
            }
        }
    ) {
        Icon(
            painter = painterResource(Res.drawable.menu),
            contentDescription = "Menu"
        )
    }
}
