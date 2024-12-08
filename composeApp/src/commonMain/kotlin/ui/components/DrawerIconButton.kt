package ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import ui.app.LocalDrawerState

@Composable
fun DrawerIconButton(drawerState: DrawerState = LocalDrawerState.current) {
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                with(drawerState) { if (isClosed) open() else close() }
            }
        }
    ) { Icon(Icons.Default.Menu, "menu") }
}
