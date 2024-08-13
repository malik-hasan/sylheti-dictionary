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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import di.LocalLocalization
import kotlinx.serialization.Serializable
import models.BN
import models.EN
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.language
import sylhetidictionary.composeapp.generated.resources.settings
import ui.components.SylhetiDictionaryTopBar
import ui.theme.bengaliDisplayFontFamily

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
    onEvent: (SettingsEvent) -> Unit,
    locale: String = LocalLocalization.current
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
                        .background(Color.Cyan)
                        .height(40.dp)
                ) {
                    val offset by animateDpAsState(
                        targetValue = if (locale == EN) {
                            0.dp
                        } else {
                            maxWidth * .48f
                        }
                    )

                    Box(
                        Modifier
                            .offset(x = offset)
                            .fillMaxWidth(0.52f)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray)
                            .fillMaxHeight()
                    )

                    Row(Modifier.selectableGroup()) {
                        Column(
                            Modifier.selectable(
                                interactionSource = null,
                                indication = null,
                                selected = locale != BN
                            ) {
                                onEvent(SettingsEvent.SetLocale(EN))
                            }.fillMaxHeight().weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("English")
                        }
                        Column(
                            Modifier.selectable(
                                interactionSource = null,
                                indication = null,
                                selected = locale == BN
                            ) {
                                onEvent(SettingsEvent.SetLocale(BN))
                            }.fillMaxHeight().weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("ক", fontFamily = bengaliDisplayFontFamily)
                            Text("Bengali")
                        }
                    }
                }
            }
        }
    }
}
