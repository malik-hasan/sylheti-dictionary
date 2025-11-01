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
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchLanguage
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchPosition
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchScript
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchEvent
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchSettingsEvent
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchSettingsState
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.also_search
import sylhetidictionary.composeapp.generated.resources.deep_search
import sylhetidictionary.composeapp.generated.resources.in_definitions
import sylhetidictionary.composeapp.generated.resources.in_examples
import sylhetidictionary.composeapp.generated.resources.search_language
import sylhetidictionary.composeapp.generated.resources.search_languages
import sylhetidictionary.composeapp.generated.resources.search_position
import sylhetidictionary.composeapp.generated.resources.search_script

@Composable
fun SearchSettingsMenu(
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    settingsState: SearchSettingsState,
    onSettingsEvent: (SearchSettingsEvent) -> Unit
) {
    DropdownMenu(
        expanded = searchState.settingsMenuOpen,
        onDismissRequest = { onSearchEvent(SearchEvent.OpenSettingsMenu(false)) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.search_position),
                    label = stringResource(Res.string.search_position),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                SingleChoiceSegmentedButtonRow {
                    with(SearchPosition.entries) {
                        forEachIndexed { i, entry ->
                            val label = stringResource(entry.label)
                            SegmentedButton(
                                modifier = Modifier.weight(label.length.coerceIn(5, 8).toFloat()),
                                selected = settingsState.position == entry,
                                onClick = { onSettingsEvent(SearchSettingsEvent.SelectPosition(entry)) },
                                shape = SegmentedButtonDefaults.itemShape(i, size)
                            ) { Text(label) }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.search_script),
                    label = stringResource(Res.string.search_script),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                SingleChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Max)) {
                    with(SearchScript.entries) {
                        forEachIndexed { i, entry ->
                            val label = stringResource(entry.label)
                            SegmentedButton(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(label.length.coerceIn(5, 7).toFloat()),
                                selected = settingsState.script == entry,
                                onClick = { onSettingsEvent(SearchSettingsEvent.SelectScript(entry)) },
                                shape = SegmentedButtonDefaults.itemShape(i, size)
                            ) { Text(label) }
                        }
                    }
                }
            }

            settingsState.languages.toList()
                .filter {
                    if (!searchState.featureBengaliDefinitions && !settingsState.featureBengaliExamples) {
                        it.first != SearchLanguage.EasternNagri.BENGALI
                    } else true
                }.run {
                    AnimatedVisibility(
                        visible = size > 1,
                        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            SettingLabel(
                                iconPainter = painterResource(Res.drawable.search_language),
                                label = stringResource(Res.string.search_languages),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            MultiChoiceSegmentedButtonRow {
                                forEachIndexed { i, (language, checked) ->
                                    SegmentedButton(
                                        checked = checked,
                                        onCheckedChange = { selected ->
                                            onSettingsEvent(SearchSettingsEvent.SelectLanguage(language, selected))
                                        },
                                        shape = SegmentedButtonDefaults.itemShape(i, size)
                                    ) { Text(stringResource(language.label)) }
                                }
                            }
                        }
                    }
                }

            Column {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.deep_search),
                    label = stringResource(Res.string.also_search),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                val easternNagriSelected = settingsState.script == SearchScript.EASTERN_NAGRI
                val bengaliEasternNagriEnabled = settingsState.languages[SearchLanguage.EasternNagri.BENGALI] == true
                val sylhetiEasternNagriEnabled = settingsState.languages[SearchLanguage.EasternNagri.SYLHETI] == true

                val onlyBengaliEasternNagriEnabled = easternNagriSelected && !sylhetiEasternNagriEnabled

                AnimatedVisibility(!easternNagriSelected || (searchState.featureBengaliDefinitions && bengaliEasternNagriEnabled)) {
                    CheckboxSearchSetting(
                        label = stringResource(Res.string.in_definitions),
                        checked = settingsState.searchDefinitions || onlyBengaliEasternNagriEnabled,
                        enabled = !onlyBengaliEasternNagriEnabled
                    ) { onSettingsEvent(SearchSettingsEvent.EnableSearchDefinitions(it)) }
                }

                AnimatedVisibility(!onlyBengaliEasternNagriEnabled || settingsState.featureBengaliExamples) {
                    val onlyBengaliEasternNagriExamplesEnabled = onlyBengaliEasternNagriEnabled && settingsState.featureBengaliExamples && !searchState.featureBengaliDefinitions
                    CheckboxSearchSetting(
                        label = stringResource(Res.string.in_examples),
                        checked = settingsState.searchExamples || onlyBengaliEasternNagriExamplesEnabled,
                        enabled = !onlyBengaliEasternNagriExamplesEnabled
                    ) { onSettingsEvent(SearchSettingsEvent.EnableSearchExamples(it)) }
                }
            }
        }
    }
}
