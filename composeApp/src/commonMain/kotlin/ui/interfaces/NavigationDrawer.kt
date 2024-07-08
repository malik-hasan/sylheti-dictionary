package ui.interfaces

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

val LocalDrawerState = compositionLocalOf { DrawerState(DrawerValue.Closed) }

@Composable
fun NavigationDrawer(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDrawerState provides DrawerState(DrawerValue.Closed)) {
        ModalNavigationDrawer(
            drawerState = LocalDrawerState.current,
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        Modifier.padding(8.dp)
                    ) {

                        Text(
                            text = "Sylheti Dictionary",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )

                        NavigationDrawerItem(
                            label = { Text("Search") },
                            selected = true,
                            onClick = { /*TODO*/ }
                        )

                        NavigationDrawerItem(
                            label = { Text("Domains") },
                            selected = false,
                            onClick = { /*TODO*/ }
                        )

                        NavigationDrawerItem(
                            label = { Text("About") },
                            selected = false,
                            onClick = { /*TODO*/ }
                        )
                    }
                }
            },
            content = content
        )
    }
}
