package ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.about
import ui.components.DrawerIconButton
import ui.components.SDScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    SDScreen(
        topBar = {
            TopAppBar(
                navigationIcon = { DrawerIconButton() },
                title = { Text(stringResource(Res.string.about)) }
            )
        }
    ) {
        Text("TODO: ask Sylheti Project for description of dictionary and organization")
    }
}
