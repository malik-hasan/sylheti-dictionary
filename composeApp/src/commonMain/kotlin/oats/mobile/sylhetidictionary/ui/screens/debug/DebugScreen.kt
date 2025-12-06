package oats.mobile.sylhetidictionary.ui.screens.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.ui.components.DebugSwitch
import oats.mobile.sylhetidictionary.ui.components.DrawerIconButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DebugScreen(
    state: DebugState,
    onEvent: (DebugEvent) -> Unit
) {
    SDScreen(
        topBar = {
            SDTopAppBar(
                navigationIcon = { DrawerIconButton() },
                title = { Text("Debug Menu") }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DebugSwitch(
                label = "Bengali App Locale",
                description = "Unlocks the setting (on settings screen) to switch the app's titles and labels to Bengali",
                checked = state.featureBengaliAppLocale,
                onCheckedChange = { onEvent(DebugEvent.EnableFeatureBengaliAppLocale(it)) }
            )

            DebugSwitch(
                label = "Bengali Definitions",
                description = "View and search Bengali dictionary definitions (when available)",
                checked = state.featureBengaliDefinitions,
                onCheckedChange = { onEvent(DebugEvent.EnableFeatureBengaliDefinitions(it)) }
            )

            DebugSwitch(
                label = "Bengali Examples",
                description = "View and search Bengali translations of example sentences (when available)",
                checked = state.featureBengaliExamples,
                onCheckedChange = { onEvent(DebugEvent.EnableFeatureBengaliExamples(it)) }
            )
        }
    }
}
