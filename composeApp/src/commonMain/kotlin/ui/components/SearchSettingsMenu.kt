package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
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
import ui.app.LocalShowNagri
import ui.screens.search.search.SearchSettingsEvent
import ui.screens.search.search.SearchSettingsState

@Composable
fun SearchSettingsMenu(
    state: SearchSettingsState,
    onEvent: (SearchSettingsEvent) -> Unit,
    showNagri: Boolean = LocalShowNagri.current
) {
    DropdownMenu(
        expanded = state.menuExpanded,
        onDismissRequest = { onEvent(SearchSettingsEvent.ToggleSettingsMenu(false)) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SingleChoiceSegmentedSearchSetting(
                settingLabel = stringResource(Res.string.search_position),
                entries = SearchPosition.entries,
                selection = state.position
            ) { onEvent(SearchSettingsEvent.SelectPosition(it)) }

            SingleChoiceSegmentedSearchSetting(
                settingLabel = stringResource(Res.string.search_script),
                entries = if (showNagri) {
                    SearchScript.entries
                } else SearchScript.entries - SearchScript.NAGRI,
                selection = state.script
            ) { onEvent(SearchSettingsEvent.SelectScript(it)) }

            with(state.languages.toList()) {
                AnimatedVisibility(isNotEmpty()) {
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

            Column {
                Text("Also search in")

                CheckboxSearchSetting("Definitions", state.searchDefinitions) {
                    onEvent(SearchSettingsEvent.ToggleSearchDefinitions(it))
                }

                CheckboxSearchSetting("Examples", state.searchExamples) {
                    onEvent(SearchSettingsEvent.ToggleSearchExamples(it))
                }
            }
        }
    }
}
