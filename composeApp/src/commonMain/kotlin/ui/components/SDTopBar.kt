package ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import di.LocalDrawerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SylhetiDictionaryTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = LocalDrawerState.current,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        with(drawerState) { if (isClosed) open() else close() }
                    }
                }
            ) { Icon(Icons.Default.Menu, "menu") }
        },
        title = { Text(title) },
        actions = actions
    )
}
