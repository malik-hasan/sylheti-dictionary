package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.ui.app.LocalDrawerState
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.menu

@Composable
fun DrawerIconButton(drawerState: DrawerState = LocalDrawerState.current) {
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                drawerState.run {
                    if (isClosed) open() else close()
                }
            }
        }
    ) {
        Icon(
            painter = painterResource(Res.drawable.menu),
            contentDescription = "Menu"
        )
    }
}
