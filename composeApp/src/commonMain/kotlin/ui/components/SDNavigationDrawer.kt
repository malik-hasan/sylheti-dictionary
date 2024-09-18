package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.settings
import di.LocalDrawerState
import ui.screens.search.SearchRoute
import ui.screens.settings.SettingsRoute

@Composable
fun SDNavigationDrawer(content: @Composable () -> Unit) {
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

                    SDNavigationDrawerItem(stringResource(Res.string.search), SearchRoute)

                    SDNavigationDrawerItem(stringResource(Res.string.settings), SettingsRoute)

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
