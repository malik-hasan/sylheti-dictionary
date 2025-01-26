package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.models.search.settings.SearchPosition
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.also_search
import sylhetidictionary.composeapp.generated.resources.in_definitions
import sylhetidictionary.composeapp.generated.resources.in_examples
import sylhetidictionary.composeapp.generated.resources.search_languages
import sylhetidictionary.composeapp.generated.resources.search_position
import sylhetidictionary.composeapp.generated.resources.search_script
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchSettingsEvent
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchSettingsState

@Composable
fun SearchSettingsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    state: SearchSettingsState,
    onEvent: (SearchSettingsEvent) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(Res.string.search_position), Modifier.padding(start = 8.dp))
                SingleChoiceSegmentedButtonRow {
                    with(SearchPosition.entries) {
                        forEachIndexed { i, entry ->
                            val label = stringResource(entry.label)
                            SegmentedButton(
                                modifier = Modifier.weight(label.length.coerceIn(5, 8).toFloat()),
                                selected = state.position == entry,
                                onClick = { onEvent(SearchSettingsEvent.SelectPosition(entry)) },
                                shape = SegmentedButtonDefaults.itemShape(i, size)
                            ) { Text(label) }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(Res.string.search_script), Modifier.padding(start = 8.dp))
                SingleChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Max)) {
                    with(SearchScript.entries) {
                        forEachIndexed { i, entry ->
                            val label = stringResource(entry.label)
                            SegmentedButton(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(label.length.coerceIn(5, 7).toFloat()),
                                selected = state.script == entry,
                                onClick = { onEvent(SearchSettingsEvent.SelectScript(entry)) },
                                shape = SegmentedButtonDefaults.itemShape(i, size)
                            ) { Text(label) }
                        }
                    }
                }
            }

            with(state.languages.toList()) {
                AnimatedVisibility(
                    visible = isNotEmpty(),
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                ) {
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
                Text(stringResource(Res.string.also_search))

                CheckboxSearchSetting(stringResource(Res.string.in_definitions), state.searchDefinitions) {
                    onEvent(SearchSettingsEvent.ToggleSearchDefinitions(it))
                }

                CheckboxSearchSetting(stringResource(Res.string.in_examples), state.searchExamples) {
                    onEvent(SearchSettingsEvent.ToggleSearchExamples(it))
                }
            }
        }
    }
}
