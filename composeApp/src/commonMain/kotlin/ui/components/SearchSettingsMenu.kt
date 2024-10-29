package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.search.settings.SearchPosition
import models.search.settings.SearchScript
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.search_languages
import sylhetidictionary.composeapp.generated.resources.search_position
import sylhetidictionary.composeapp.generated.resources.search_script
import ui.screens.search.SearchSettingsEvent
import ui.screens.search.SearchSettingsState

@Composable
fun SearchSettingsMenu(
    state: SearchSettingsState,
    onEvent: (SearchSettingsEvent) -> Unit
) {

    DropdownMenu(
        expanded = state.menuExpanded,
        onDismissRequest = { onEvent(SearchSettingsEvent.ToggleSettingsMenu(false)) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(Res.string.search_position), Modifier.padding(start = 8.dp))
                SingleChoiceSegmentedButtonRow {
                    with(SearchPosition.entries) {
                        forEachIndexed { i, position ->
                            val label = stringResource(position.label)
                            SegmentedButton(
                                modifier = Modifier.weight(label.length.toFloat().coerceIn(5f, 8f)),
                                selected = state.position == position,
                                onClick = { onEvent(SearchSettingsEvent.SelectPosition(position)) },
                                shape = SegmentedButtonDefaults.itemShape(i, size)
                            ) { Text(label) }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(Res.string.search_script), Modifier.padding(start = 8.dp))
                SingleChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Min)) {
                    with(SearchScript.entries) {
                        forEachIndexed { i, script ->
                            val label = stringResource(script.label)
                            SegmentedButton(
                                modifier = Modifier.weight(label.length.toFloat().coerceIn(5f, 8f)),
                                selected = state.script == script,
                                onClick = { onEvent(SearchSettingsEvent.SelectScript(script)) },
                                shape = SegmentedButtonDefaults.itemShape(i, size)
                            ) { Text(label) }
                        }
                    }
                }
            }

            with(state.languages.toList()) {
                if (isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(stringResource(Res.string.search_languages), Modifier.padding(start = 8.dp))
                        MultiChoiceSegmentedButtonRow {
                            forEachIndexed { i, (language, checked) ->
                                SegmentedButton(
                                    checked = checked,
                                    onCheckedChange = { selected ->
                                        onEvent(SearchSettingsEvent.SelectLanguage(language, selected))
                                    },
                                    shape = SegmentedButtonDefaults.itemShape(i, size)
                                ) { Text(stringResource(language.label)) }
                            }
                        }
                    }
                }
            }
        }
    }
}
