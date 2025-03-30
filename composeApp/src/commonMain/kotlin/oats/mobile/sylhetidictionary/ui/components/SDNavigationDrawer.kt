package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.models.Route
import oats.mobile.sylhetidictionary.ui.app.LocalDrawerState
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.about
import sylhetidictionary.composeapp.generated.resources.ipa_help
import sylhetidictionary.composeapp.generated.resources.search
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.sylheti_dictionary

@Composable
fun SDNavigationDrawer(
    drawerState: DrawerState = LocalDrawerState.current,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            BackHandler(drawerState.isOpen) {
                scope.launch {
                    drawerState.close()
                }
            }

            ModalDrawerSheet {
                Column(Modifier
                    .safeDrawingPadding()
                    .padding(8.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(Res.string.sylheti_dictionary),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    SDNavigationDrawerItem(stringResource(Res.string.search), Route.Search)

                    SDNavigationDrawerItem(stringResource(Res.string.settings), Route.Settings)

                    SDNavigationDrawerItem(stringResource(Res.string.ipa_help), Route.IpaHelp)

                    SDNavigationDrawerItem(stringResource(Res.string.about), Route.About)

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "Powered by Oats Mobile",
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
                    )
                }
            }
        },
        content = content
    )
}
