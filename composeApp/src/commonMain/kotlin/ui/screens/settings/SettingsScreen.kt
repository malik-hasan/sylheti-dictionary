package ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable
import models.Locale
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.settings
import ui.components.SylhetiDictionaryTopBar

@Serializable
object SettingsRoute

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SettingsScreen(vm: SettingsViewModel = koinViewModel()) {
    SettingsScreen(vm::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onEvent: (SettingsEvent) -> Unit
) {
    Scaffold(
        topBar = {
            SylhetiDictionaryTopBar(stringResource(Res.string.settings))
        }
    ) {
        Column(Modifier.padding(it)) {
            Button(onClick = { onEvent(SettingsEvent.SetLocale(Locale.English)) }) {
                Text("English")
            }
            Button(onClick = { onEvent(SettingsEvent.SetLocale(Locale.Bengali)) }) {
                Text("Bengali")
            }
            Button(onClick = {}) {
                Text("App Settings")
            }
        }
    }
}
