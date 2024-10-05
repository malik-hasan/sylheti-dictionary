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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import di.LocalLanguage
import kotlinx.serialization.Serializable
import models.Language
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.language
import sylhetidictionary.composeapp.generated.resources.settings
import ui.components.DynamicThemeSetting
import ui.components.LanguageButton
import ui.components.SylhetiDictionaryTopBar

@Composable
fun SettingsScreen(vm: SettingsViewModel = koinViewModel()) {
    SettingsScreen(vm::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onEvent: (SettingsEvent) -> Unit,
    locale: Language = LocalLanguage.current
) {

    Scaffold(
        topBar = { SylhetiDictionaryTopBar(stringResource(Res.string.settings)) }
    ) { scaffoldPadding ->

        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painterResource(Res.drawable.language),
                    stringResource(Res.string.language),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(stringResource(Res.string.language), style = MaterialTheme.typography.bodyLarge)
            }

            BoxWithConstraints(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .height(150.dp)
            ) {
                val offset by animateDpAsState(if (locale == Language.BN) maxWidth * .48f else 0.dp)

                // Indicator
                Box(
                    Modifier
                        .offset(offset)
                        .fillMaxWidth(0.52f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxHeight()
                )

                Row(Modifier.selectableGroup()) {
                    Language.entries.forEach { language ->
                        LanguageButton(
                            language = language,
                        ) { onEvent(SettingsEvent.SetLanguage(language)) }
                    }
                }
            }

            DynamicThemeSetting { onEvent(SettingsEvent.ToggleDynamicTheme(it)) }
        }
    }
}
