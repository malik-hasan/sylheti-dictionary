package ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import di.setLanguage
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import models.Language
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.settings
import ui.components.SylhetiDictionaryTopBar

@Serializable
object SettingsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SylhetiDictionaryTopBar(stringResource(Res.string.settings))
        }
    ) {
        Column(Modifier.padding(it)) {
            Button(onClick = {
                scope.launch {
                    setLanguage(Language.English.code)
                }
            }) {
                Text("English")
            }
            Button(onClick = {
                scope.launch {
                    setLanguage(Language.Bengali.code)
                }
            }) {
                Text("Bengali")
            }
        }
    }
}
