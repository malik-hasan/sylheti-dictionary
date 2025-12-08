package oats.mobile.sylhetidictionary.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import oats.mobile.sylhetidictionary.ui.components.NavigationRailIconButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBar
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.about

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutScreen() {
    SDScreen(
        topBar = {
            SDTopAppBar(
                navigationIcon = { NavigationRailIconButton() },
                title = { Text(stringResource(Res.string.about)) }
            )
        }
    ) { scaffoldPadding ->
        Text(
            text = "TODO: ask Sylheti Project for description of dictionary and organization",
            modifier = Modifier.padding(scaffoldPadding)
        )
    }
}
