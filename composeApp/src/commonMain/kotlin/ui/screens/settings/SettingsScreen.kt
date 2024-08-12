package ui.screens.settings

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import models.Locale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.language
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
        topBar = { SylhetiDictionaryTopBar(stringResource(Res.string.settings)) }
    ) { scaffoldPadding ->

        Column(
            Modifier
                .padding(scaffoldPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            var selected by remember { mutableStateOf(true) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painterResource(Res.drawable.language),
                    "Language",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text("Language", style = MaterialTheme.typography.titleSmall)
            }

            Surface {
                BoxWithConstraints(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Green)
                        .height(40.dp)
                ) {
                    val offset by animateDpAsState(
                        targetValue = if (selected) {
                            0.dp
                        } else {
                            maxWidth * .48f
                        }
                    )

                    Box(
                        Modifier
                            .offset(x = offset)
                            .shadow(1.dp, RoundedCornerShape(10.dp))
                            .fillMaxWidth(0.52f)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray)
                            .fillMaxHeight()
                    )

                    Row(Modifier.selectableGroup()) {
                        Box(
                            Modifier.selectable(
                                interactionSource = null,
                                indication = null,
                                selected = selected
                            ) {
                                selected = true
                                onEvent(SettingsEvent.SetLocale(Locale.English))
                            }.fillMaxHeight().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("English")
                        }
                        Box(
                            Modifier.selectable(
                                interactionSource = null,
                                indication = null,
                                selected = !selected
                            ) {
                                selected = false
                                onEvent(SettingsEvent.SetLocale(Locale.Bengali))
                            }.fillMaxHeight().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Bengali")
                        }
                    }
                }
            }
        }
    }
}
