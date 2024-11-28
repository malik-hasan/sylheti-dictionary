package ui.screens.settings

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import models.Language
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.language
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.show_sylheti_nagri
import sylhetidictionary.composeapp.generated.resources.sylheti_nagri
import ui.app.LocalLanguage
import ui.components.DynamicThemeSetting
import ui.components.LanguageButton
import ui.components.SettingLabel
import ui.components.SylhetiDictionaryTopBar

@Composable
fun SettingsScreen(vm: SettingsViewModel = koinViewModel()) {
    val state by vm.settingsState.collectAsStateWithLifecycle()

    SettingsScreen(state, vm::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    language: Language = LocalLanguage.current
) {

    Scaffold(
        topBar = { SylhetiDictionaryTopBar(stringResource(Res.string.settings)) }
    ) { scaffoldPadding ->

        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.language),
                    label = stringResource(Res.string.language)
                )

                BoxWithConstraints(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .height(150.dp)
                ) {
                    val offset by animateDpAsState(
                        targetValue = if (language == Language.BN) maxWidth * .48f else 0.dp,
                        animationSpec = tween()
                    )

                    // Indicator
                    Box(
                        Modifier
                            .offset(offset)
                            .fillMaxWidth(0.52f)
                            .fillMaxHeight()
                            .padding(8.dp)
                            .shadow(8.dp, MaterialTheme.shapes.extraLarge)
                            .background(MaterialTheme.colorScheme.primary)
                    )

                    Row(Modifier.selectableGroup()) {
                        Language.entries.forEach { language ->
                            LanguageButton(language) {
                                onEvent(SettingsEvent.SetLanguage(language))
                            }
                        }
                    }
                }
            }

            DynamicThemeSetting(state.dynamicThemeEnabled) {
                onEvent(SettingsEvent.ToggleDynamicTheme(it))
            }

            Column {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.sylheti_nagri),
                    label = stringResource(Res.string.show_sylheti_nagri)
                )

                Switch(
                    checked = state.showNagriEnabled,
                    onCheckedChange = { onEvent(SettingsEvent.ToggleShowNagri(it)) }
                )
            }
        }
    }
}
