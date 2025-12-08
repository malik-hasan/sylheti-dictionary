package oats.mobile.sylhetidictionary.ui.screens.settings

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Language
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Theme
import oats.mobile.sylhetidictionary.ui.app.LocalLanguage
import oats.mobile.sylhetidictionary.ui.components.NavigationRailIconButton
import oats.mobile.sylhetidictionary.ui.components.DynamicThemeSetting
import oats.mobile.sylhetidictionary.ui.components.LanguageButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBar
import oats.mobile.sylhetidictionary.ui.components.SettingLabel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.contrast
import sylhetidictionary.composeapp.generated.resources.language
import sylhetidictionary.composeapp.generated.resources.settings
import sylhetidictionary.composeapp.generated.resources.theme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    language: Language = LocalLanguage.current
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    SDScreen(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SDTopAppBar(
                navigationIcon = { NavigationRailIconButton() },
                title = { Text(stringResource(Res.string.settings)) },
                scrollBehavior = scrollBehavior
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
            if (state.featureBengaliAppLocale) {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.language),
                    label = stringResource(Res.string.language)
                )

                BoxWithConstraints(Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .height(150.dp)
                    .widthIn(max = 1000.dp)
                ) {
                    val offset by animateDpAsState(
                        targetValue = if (language == Language.BN) maxWidth * .48f else 0.dp,
                        animationSpec = tween()
                    )

                    // Indicator
                    Box(Modifier
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

            SettingLabel(
                iconPainter = painterResource(Res.drawable.contrast),
                label = stringResource(Res.string.theme)
            )

            SingleChoiceSegmentedButtonRow(Modifier
                .widthIn(max = 1000.dp)
                .fillMaxWidth()
            ) {
                with(Theme.entries) {
                    forEachIndexed { i, entry ->
                        val label = stringResource(entry.label)
                        SegmentedButton(
                            modifier = Modifier.weight(label.length.toFloat().coerceIn(5f, 8f)),
                            selected = state.theme == entry,
                            onClick = { onEvent(SettingsEvent.SelectTheme(entry)) },
                            shape = SegmentedButtonDefaults.itemShape(i, size)
                        ) { Text(label) }
                    }
                }
            }

            DynamicThemeSetting(state.dynamicThemeEnabled) {
                onEvent(SettingsEvent.EnableDynamicTheme(it))
            }
        }
    }
}
